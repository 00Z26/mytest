import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Http2 {
    public static void main(String args[]) throws IOException {
        int port = 8082;
        new Http2().start(port);
    }

    public void start(int port) throws IOException {
        ServerSocket server = new ServerSocket(port);

        //说明strat()调用成功
        System.out.println("server starts at " + port + ":  [################]100%");
        while(true) {
            //调用线程开始emm...运行
            Socket client = server.accept();
            ServerThread serverThread = new ServerThread(client, server);
            serverThread.start();
        }
    }

    class ServerThread extends Thread {
        Socket client;
        ServerSocket server;

        public ServerThread(Socket client, ServerSocket server) {
            this.client = client;
            this.server = server;
        }

        //获取协议内容String
        private String getcont(InputStream in) {
            //把输入流内容转化为byte数组
            byte[] buf = new byte[1024];
            int len = 0;
            try {
                len = in.read(buf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //协议内容string
            String text = null;
            try {
                text = new String(buf, 0, len, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return text;
        }

        //输出http协议
        //System.out.println(text);
        //in是浏览器输入的内容
        // out是服务器输出的内容
        //分析url中请求的文件
        //String url;
        private String getqs(String text) {
            String queryresource = null;
            text = text.substring(text.indexOf('/'));
            text = text.substring(0, text.indexOf(' '));
            //System.out.println(text);
            int index = text.indexOf('?');
            //System.out.println(index);
            if (index != -1) {
                queryresource = text.substring(0, text.indexOf('?'));
            } else {
                queryresource = text;
            }

            index = queryresource.lastIndexOf("/");

            if (index + 1 == queryresource.length()) {
                queryresource = queryresource + "index.html";
            } else {
                String filename = queryresource.substring(index + 1);
                if (!filename.contains("."))
                    queryresource = queryresource + ".html";
            }
            return queryresource;
        }

        //设定文件响应头
        private String gethd(String qs) {
            int index = qs.lastIndexOf("/");
            String head;
            String filename = "";
            filename = qs.substring(index + 1);
            String[] filetypes = filename.split("\\.");
            String filetype = filetypes[filetypes.length - 1];

            if (filetype.equals("html")) {
                head = "HTTP/1.1200OK\n" + "Content-Type:text/html\n" + "Server:myserver\n" + "\n";
            } else if (filetype.equals("jpg") || filetype.equals("gif") || filetype.equals("png")) {
                head = "HTTP/1.1200OK\n" + "Content-Type:image/jpeg\n" + "Server:myserver\n" + "\n";
            } else {
                head = "HTTP/1.1404NotFound\n" + "Sever:myserver" + "\n";
            }
            return head;
        }

        //对应文件返回为byte型
        private byte[] getFB(String Filename) throws IOException {
            File file = new File(Filename);
            ByteArrayOutputStream by = new ByteArrayOutputStream();

            try {
                FileInputStream fileb = new FileInputStream(file);
                byte[] b = new byte[1000];
                int read;
                while ((read = fileb.read(b)) != -1) {
                    by.write(b, 0, read);
                }
                fileb.close();
                by.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return by.toByteArray();
        }

        public void run() {
            InputStream in = null;
            OutputStream out = null;
            // 监听到之后输出客户端地址
            // System.out.println(client.getInetAddress().getHostAddress());
            try {
                //获取输出流的内容
                in = client.getInputStream();
                out = client.getOutputStream();
                String content = getcont(in);
                String qs = getqs(content);
                String hd = gethd(qs);
                while (true) {
                    //要做一个未找到资源返回404错误，待施工
                    if (hd == "HTTP/1.1404NotFound\n" + "Sever:myserver" + "\n") {
                        assert out != null;
                        try {
                            out.write(hd.getBytes("utf-8"));
                            out.close();
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //打开对应文件夹的文件，把文件转化为byte数组
                        byte data[] = new byte[0];
                        data = getFB("here" + qs);
                        if (data != null) {
                            //在输出流写入
                            try {
                                assert out != null;
                                out.write(hd.getBytes("utf-8"));
                                out.write(data);
                                out.close();
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}










