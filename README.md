Đây là ứng dụng quản lý khách sạn cơ bản
Cách sử dụng:
 - Clone code về.
 - Điều kiện để chạy được code :
   + Phải kết nối với Cơ sở dữ liệu (Workbench,...) (Sẽ có code tạo bảng ở trên)
   + Chỉnh sửa đường link, mật khẩu để kết nối tới database của mình (Chỉnh ở file DatabaseConnection)
      private static final String URL = "jdbc:mysql://localhost:3306/hotel"; chỉnh thành của bạn
      private static final String USER = "root"; chỉnh thành của bạn
      private static final String PASSWORD = "1234"; chỉnh thành của bạn
   + Chạy bằng file MainForm
