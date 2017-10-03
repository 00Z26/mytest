import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import static com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility.getBytes;

public class Http2 {
        public static void main(String args[])throws IOException {
            int port = 8081;
            new Http1().start(port);
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
                //InputStream in = null;
                OutputStream out = null;
//            监听到之后输出客户端地址
//            System.out.println(client.getInetAddress().getHostAddress());
                try {
                    //获取输出流的内容
                    // in = client.getInputStream();
                    out = client.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //把输入流内容转化为byte数组
//            byte [] buf=new byte[1024];
//            int len= 0;
//            try {
//                len = in.read(buf);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
                //协议内容string
//            String text= null;
//            try {
//                text = new String(buf,0,len,"utf-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
                // 输出http协议
                //System.out.println(text);

                //定义要输出字节
                byte data[];
                data = getBytes("Hello,I love you ~OVO");
                //响应的头
                String head =  "HTTP/1.1200OK\n"+"Content-Type:text/html\n" + "Server:myserver\n" + "\n";
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


