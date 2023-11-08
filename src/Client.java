

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static DataOutputStream dos = null;
    static DataInputStream dis = null;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        Scanner sc = new Scanner(System.in);
        try {
            socket.connect(new InetSocketAddress("192.168.0.201", 50001));
            System.out.println("연결 시도중 ... 포트번호 : " + socket.getLocalPort());
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            System.out.print("사용자 아이디를 만들어 주새요 : ");
            dos.writeUTF(sc.nextLine());

            String mab = dis.readUTF();
            System.out.println(mab);
            System.out.print(">>");
            dos.write(sc.nextInt());

            //서버로부터 데이터를 읽는 로직
            new Thread(() -> {
                try {
                    while (true) {
                        String meseeage = dis.readUTF();
                        System.out.println(meseeage);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();


            while (true) {
                System.out.print(">>");
                dos.writeUTF(sc.nextLine());
                dos.flush();
            }
        }catch (IOException e){
            throw new RuntimeException();
        } finally {
            try {
                dos.close();
                socket.close();
            }catch (IOException e){
                throw new RuntimeException();
            }
        }
    }
}