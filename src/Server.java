import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static ArrayList<Socket> sockets = new ArrayList<>();
    static  int[] baseBall = null;
    static ServerSocket server = null;
    static int count =0;
    public static void main(String[] args) {
        try {
            server = new ServerSocket(50001);
            while(true) {
                System.out.println("대기");
                Socket socket = server.accept(); // 2명만 접속허용
                count++;
                if (count <= 2) {
//                    game();
                    count += 2;
                }

            }



//            sockets.add(socket);
//
//            sockets.remove(socket);
//            socket.close();

        }catch (Exception e){

        }
    }
    public static void game(Socket socket){
        DataInputStream dis = null;
        DataOutputStream dos = null;
        String id = "";
        InetSocketAddress isa = (InetSocketAddress)  socket.getRemoteSocketAddress();
        System.out.println("[서버] " + isa.getHostName() + "의 연결 요청을 수락함");
        try {
            dis = new DataInputStream(socket.getInputStream());
            id = dis.readUTF();
            String message = "";
            while (true) {
                message = dis.readUTF();
                System.out.println(id+ " : " + message);


            }
        }catch (Exception e){
            try {
                System.out.println(id + "님이 나가셨습니다. ");
                socket.close();
            }catch (IOException ex){
                throw new RuntimeException(ex);
            }
        }
    }
//    public int getInt(String s){
//        try {
//            System.out.println(s);
//
//        }
//    }
}
