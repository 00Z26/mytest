import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import static com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility.getBytes;

    public class Http2 {
        public static void main(String args[])throws IOException {
            int port = 8082;
            new Http2().start(port);
        }

        public void start(int port)throws IOException {
            ServerSocket server = new ServerSocket(port);
            //说明strat()调用成功
            System.out.println("server starts at "+port+":  [################]100%");
            Socket client = server.accept();
            //调用线程开始emm...运行
            ServerThread serverThread = new ServerThread(client, server);
            serverThread.start();

        }

        class ServerThread extends Thread{
            Socket client;
            ServerSocket server;

            public ServerThread (Socket client,ServerSocket server){
                this.client = client;
                this.server = server;
            }

            public void run(){
                InputStream in = null;
                OutputStream out = null;
           // 监听到之后输出客户端地址
                // System.out.println(client.getInetAddress().getHostAddress());
                try {
                    //获取输出流的内容
                     in = client.getInputStream();
                    out = client.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //把输入流内容转化为byte数组
                byte [] buf=new byte[1024];
                int len= 0;
                try {
                    len = in.read(buf);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //协议内容string
            String text= null;
            try {
                text = new String(buf,0,len,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
                 //输出http协议
                //System.out.println(text);
                //in是浏览器输入的内容
                // out是服务器输出的内容
                //分析url中请求的文件
                //String url;
                String queryresource = null;
                text = text.substring(text.indexOf('/'));
                text = text.substring(0,text.indexOf(' '));
                //System.out.println(text);
                int index = text.indexOf('?');
                //System.out.println(index);
                if(index != -1){
                    queryresource = text .substring(0,text.indexOf('?'));
                }
                else{
                    queryresource = text;
                }
                index = queryresource.lastIndexOf("/");

                if(index+1==queryresource.length())
                {
                    queryresource = queryresource+"index.html";
                }
                else
                {
                    String filename = queryresource.substring(index+1);
                    if( !filename.contains("."))
                        queryresource = queryresource+".html";
                }

                //设定文件响应头
                String head;
                index = queryresource.lastIndexOf("/");
                String filename="";
                filename=queryresource.substring(index+1);
                String[] filetypes=filename.split("\\.");
                String filetype=filetypes[filetypes.length-1];
                if(filetype.equals("html"))
                {
                    head = "HTTP/1.0200OK\n"+"Content-Type:text/html\n" + "Server:myserver\n" + "\n";
                }
                else if(filetype.equals("jpg")||filetype.equals("gif")||filetype.equals("png"))
                {
                    head =  "HTTP/1.0200OK\n"+"Content-Type:image/jpeg\n" + "Server:myserver\n" + "\n";
                }
                else{
                    head = "HTTP/1.1403Forbidden\n"+"Sever:myserver" + "\n";
                }

                //打开对应文件夹的文件，把文件转化为byte数组
                byte data[];
                ByteArrayOutputStream by =new ByteArrayOutputStream();
                String Filename = "here"+queryresource;
                File file = new File(Filename);
                try {
                   FileInputStream fileb = new FileInputStream(file);
                   byte[] b=new byte[1000];
                   int read;
                   while((read=fileb.read(b))!=-1)
                   {
                       by.write(b,0,read);
                   }
                   fileb.close();
                   by.close();
                } catch(IOException e) {
                   e.printStackTrace();
                }
                data = by.toByteArray();

                //定义要输出字节

//                data = getBytes("Hello,I love you ~OVO");
                //响应的头
                //String head =  "HTTP/1.1200OK\n"+"Content-Type:text/html\n" + "Server:myserver\n" + "\n";
                try {
                    //在输出流写入
                    assert out != null;
                    out.write(head.getBytes("utf-8"));
                    out.write(data);
                    out.close();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }




    }


