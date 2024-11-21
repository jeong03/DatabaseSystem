import java.sql.*;
import java.util.Scanner;

public class Homework {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.56.101:4567/madang", "haejeong", "1234")) {
            Class.forName("com.mysql.cj.jdbc.Driver");

            while (true) {
                System.out.println("1. 데이터 삽입하기");
                System.out.println("2. 데이터 삭제하기");
                System.out.println("3. 데이터 검색하기");
                System.out.println("4. 종료하기");
                System.out.print("선택해주세요: ");

                int number = scanner.nextInt();
                scanner.nextLine();

                switch (number) {
                    // 1. 데이터 삽입하기
                    case 1:
                        System.out.print("책 제목 입력: ");
                        String bookname = scanner.nextLine();
                        System.out.print("출판사 입력: ");
                        String publisher = scanner.nextLine();
                        System.out.print("가격 입력: ");
                        int price = scanner.nextInt();

                        String insertQuery = "INSERT INTO Book (bookname, publisher, price) VALUES (?, ?, ?)";
                        try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                            pstmt.setString(1, bookname);
                            pstmt.setString(2, publisher);
                            pstmt.setInt(3, price);
                            int rowsAffected = pstmt.executeUpdate();
                            System.out.println("삽입 완료");
                        }
                        break;

                    // 2. 데이터 삭제하기
                    case 2:
                        System.out.print("삭제할 책의 ID 입력: ");
                        int bookId = scanner.nextInt();

                        String deleteQuery = "DELETE FROM Book WHERE bookid = ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                            pstmt.setInt(1, bookId);
                            int rowsAffected = pstmt.executeUpdate();
                            if (rowsAffected > 0) {
                                System.out.println("삭제 완료");
                            } else {
                                System.out.println("해당 ID는 존재하지 않습니다. 다시 입력해주세요.");
                            }
                        }
                        break;

                    // 3. 데이터 검색하기
                    case 3:
                        System.out.print("책 제목 입력: ");
                        String searchBookname = scanner.nextLine();

                        String searchQuery = "SELECT * FROM Book WHERE bookname LIKE ?";
                        try (PreparedStatement pstmt = conn.prepareStatement(searchQuery)) {
                            pstmt.setString(1, "%" + searchBookname + "%");
                            ResultSet rs = pstmt.executeQuery();

                            while (rs.next()) {
                                int id = rs.getInt("bookid");
                                String name = rs.getString("bookname");
                                String pub = rs.getString("publisher");
                                int bookPrice = rs.getInt("price");
                                System.out.println("ID: " + id + ", 제목: " + name + ", 출판사: " + pub + ", 가격: " + bookPrice);
                            }
                        }
                        break;

                    // 4. 종료하기
                    case 4:
                        System.out.println("종료");
                        scanner.close();
                        return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
