import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Http3 {
    public static void main(String args[]) throws IOException {
        int port = Set.getPort();
        new Http3().start(port);
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
        public String getcont(InputStream in) {
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

        //对应文件返回为byte型
        private byte[] getFB(String Filename) throws IOException {
            File file = new File(Filename);
            ByteArrayOutputStream by = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            if(file.exists()) {
                try {
                    FileInputStream fileb = new FileInputStream(file);
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
            }else{
                return null;
            }
        }
        public void run() {
            //in是浏览器输入的内容
            // out是服务器输出的内容
            InputStream in = null;
            OutputStream out = null;
            // 监听到之后输出客户端地址
            // System.out.println(client.getInetAddress().getHostAddress());
            try {
                //获取输出流的内容
                in = client.getInputStream();
                out = client.getOutputStream();
                //定义响应头
                //String hd;
                //System.out.println(content);
                while(true) {
                    /**
                     * content是协议内容
                     * 改变content的请求头发往目标服务器
                     */
                    String content = getcont(in);
                    String target;
                    target = content.replace(Set.getFromserver(), Set.getTargetserver());
                    target = target.replace("keep-alive", "close");
                    target = target.replace("gzip", "");
                    //输出更改后的请求
                    //System.out.println(target);
                    /**
                     * 新建连接发送更改后的请求
                     */
                    Socket ss = new Socket(Set.getTargetserver(),80);
                    OutputStream otr = ss.getOutputStream();
                    InputStream ir = ss.getInputStream();
                    otr.write(target.getBytes());
                    String texr = getcont(ir);
                    /**
                     * 改变响应头内容
                     * 由浏览器展示出来
                     */
                    texr = texr.replace(Set.getTargetserver(),Set.getFromserver());
                    //System.out.println(texr);
                    out.write(texr.getBytes());
                    out.close();
                    otr.close();
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
