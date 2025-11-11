import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;
import model.NhanVien;
import model.QuanLyPhong;
import model.ThanhToan;
// Import các lớp Repository
import repository.IKhachHangRepository;
import repository.KhachHangRepository;
// (Sau này bạn sẽ import thêm INhanVienRepository, NhanVienRepository...)

public class Main {
    private static Scanner sc = new Scanner(System.in);

    private static IKhachHangRepository khachHangRepo = new KhachHangRepository();

    private static List<KhachHang> dsKhachHang = new ArrayList<>();

    public static void main(String[] args) {

        // GHI CHÚ QUAN TRỌNG:
        // Bạn cần một bước đồng bộ ID (Person.cnt) tại đây
        // bằng cách đọc MaKH/MaNV lớn nhất từ CSDL và set Person.setCnt()
        // (Tạm thời bỏ qua bước này để tập trung vào chức năng CRUD)

        // Tải danh sách khách hàng từ CSDL lên bộ nhớ đệm khi khởi động
        capNhatDanhSachKhachTuDB();

        int choice;
        do {
            System.out.println("\n===== MENU CHINH =====");
            System.out.println("1. Quan ly khach hang");
            System.out.println("2. Quan ly phong");
            System.out.println("3. Quan ly thanh toan");
            System.out.println("4. Quan ly nhan vien");
            System.out.println("0. Thoat");
            System.out.print("Chon: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> menuKhachHang();
                case 2 -> menuPhong();
                case 3 -> menuThanhToan();
                case 4 -> menuNhanVien(); // (Menu này cũng cần được sửa lại)
                case 0 -> System.out.println("Tam biet!");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    // ===== MENU KHÁCH HÀNG (ĐÃ SỬA ĐỂ DÙNG REPOSITORY VÀ THÊM TÌM KIẾM) =====
    private static void menuKhachHang() {
        int choice;
        do {
            System.out.println("\n===== QUAN LY KHACH HANG =====");
            System.out.println("1. Them khach hang ");
            System.out.println("2. Xoa khach hang ");
            System.out.println("3. Sua thong tin khach hang ");
            System.out.println("4. Xem danh sach khach hang ");
            System.out.println("5. Tim kiem khach hang ");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Nhap ten khach hang: ");
                    String ten = sc.nextLine();
                    System.out.print("Nhap so CMND: ");
                    String cmnd = sc.nextLine();
                    System.out.print("Nhap so dien thoai: ");
                    String sdt = sc.nextLine();
                    System.out.print("Nhap email: ");
                    String email = sc.nextLine();

                    KhachHang kh = new KhachHang(ten, cmnd, sdt, email);

                    if (khachHangRepo.add(kh)) {
                        System.out.println("Them khach hang thanh cong! (ID: " + kh.getMaID() + ")");
                        capNhatDanhSachKhachTuDB();
                    } else {
                        System.out.println("Them khach hang that bai!");
                    }
                }
                case 2 -> {
                    // ... (Code Xóa khách hàng giữ nguyên) ...
                    System.out.print("Nhap ma khach hang can xoa (vi du: KH001): ");
                    String id = sc.nextLine();

                    if (khachHangRepo.delete(id)) {
                        System.out.println("Da xoa khach hang " + id);
                        capNhatDanhSachKhachTuDB();
                    } else {
                        System.out.println("Khong tim thay hoac khong the xoa khach hang " + id);
                    }
                }
                case 3 -> {
                    System.out.print("Nhap ma khach hang can sua: ");
                    String id = sc.nextLine();

                    KhachHang kh = khachHangRepo.getById(id);

                    if (kh == null) {
                        System.out.println("Khong tim thay khach hang nao co ma " + id);
                        return; // Sửa: Dùng return thay vì break
                    }

                    System.out.println("Nhap thong tin moi cho KH: " + kh.getTen() + " (ID: " + kh.getMaID() + ")");
                    System.out.print("Ten moi (Cu: " + kh.getTen() + "): ");
                    String ten = sc.nextLine();
                    if (!ten.isEmpty())
                        kh.setTen(ten);

                    System.out.print("CMND moi (Cu: " + kh.getSoCMND() + "): ");
                    String cmnd = sc.nextLine();
                    if (!cmnd.isEmpty())
                        kh.setSoCMND(cmnd);

                    System.out.print("SDT moi (Cu: " + kh.getSoDienThoai() + "): ");
                    String sdt = sc.nextLine();
                    if (!sdt.isEmpty())
                        kh.setSoDienThoai(sdt);

                    System.out.print("Email moi (Cu: " + kh.getEmail() + "): ");
                    String email = sc.nextLine();
                    if (!email.isEmpty())
                        kh.setEmail(email);

                    if (khachHangRepo.update(kh)) {
                        System.out.println("Cap nhat thanh cong!");
                        capNhatDanhSachKhachTuDB();
                    } else {
                        System.out.println("Cap nhat that bai!");
                    }
                }
                case 4 -> {
                    // ... (Code Xem danh sách giữ nguyên) ...
                    List<KhachHang> dsTuDB = khachHangRepo.getAll();

                    if (dsTuDB.isEmpty()) {
                        System.out.println("Chua co khach hang nao ");
                        break;
                    }

                    System.out.println("\n===== DANH SACH KHACH HANG =====");
                    for (KhachHang kh : dsTuDB) {
                        System.out.println(kh);
                    }
                    System.out.println("=========================================\n");
                }

                // ===== CHỨC NĂNG MỚI =====
                case 5 -> {
                    System.out.print("Nhap ten khach hang can tim: ");
                    String ten = sc.nextLine();

                    // Sử dụng phương thức getById từ repository
                    KhachHang kh = khachHangRepo.getByName(ten);

                    if (kh != null) {
                        System.out.println("Da tim thay khach hang:");
                        System.out.println(kh); // In thông tin khách hàng
                    } else {
                        System.out.println("Khong tim thay khach hang co ten: " + ten);
                    }
                }
                // ==========================

                case 0 -> System.out.println("Quay lai menu chinh");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    // ===== MENU PHÒNG =====
    private static void menuPhong() {
        int choice;
        do {
            System.out.println("\n===== QUAN LY PHONG =====");
            System.out.println("1. Xem danh sach phong");
            System.out.println("2. Dat phong");
            System.out.println("3. Tra phong va thanh toan");
            System.out.println("4. Xem khach thue theo phong");
            System.out.println("5. Xoa phong");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> QuanLyPhong.xemDanhSachPhong();
                // Chức năng này sử dụng dsKhachHang (cache) đã được tải từ CSDL
                case 2 -> QuanLyPhong.datPhong(dsKhachHang);
                case 3 -> {
                    ThanhToan.thanhToanPhong();
                }
                case 4 -> QuanLyPhong.xemKhachTheoPhong();
                case 5 -> QuanLyPhong.xoaPhong();
                case 0 -> System.out.println("Quay lai menu chinh");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    // ===== MENU THANH TOÁN =====
    private static void menuThanhToan() {
        // (Giữ nguyên không thay đổi)
        int choice;
        do {
            System.out.println("\n===== QUAN LY THANH TOAN =====");
            System.out.println("1. Xem lich su thanh toan");
            System.out.println("2. Xem tong doanh thu");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> ThanhToan.xemLichSuThanhToan();
                case 2 -> ThanhToan.xemDoanhThu();
                case 0 -> System.out.println("Quay lai menu chinh");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice != 0);
    }

    // ===== MENU NHÂN VIÊN (Cần được sửa tương tự menuKhachHang) =====
    private static void menuNhanVien() {
        System.out.println("\n===== QUAN LY NhanVien =====");
        System.out.println("GHI CHU: Chuc nang nay can duoc nang cap de dung Repository giong nhu Quan Ly Khach Hang.");
        // (Code cũ tạm thời giữ lại)
        int choice1;
        do {
            System.out.println("1. Them nhan vien");
            System.out.println("2. Xoa nhan vien");
            System.out.println("3. Sua thong tin nhan vien");
            System.out.println("4. Xem danh sach nhan vien");
            System.out.println("0. Quay lai");
            System.out.print("Chon: ");
            choice1 = Integer.parseInt(sc.nextLine());

            switch (choice1) {
                case 1 -> NhanVien.themNhanVien();
                case 2 -> NhanVien.xoaNhanVien();
                case 3 -> NhanVien.suaNhanVien();
                case 4 -> NhanVien.xemNhanVien();
                case 0 -> System.out.println("Quay lai menu chinh");
                default -> System.out.println("Lua chon khong hop le!");
            }
        } while (choice1 != 0);
    }

    // ===== CẬP NHẬT DANH SÁCH KHÁCH HÀNG (ĐÃ SỬA) =====
    // Hàm này thay thế hàm dùng Reflection cũ
    private static void capNhatDanhSachKhachTuDB() {
        try {
            // Lấy danh sách khách hàng mới nhất từ CSDL
            dsKhachHang = khachHangRepo.getAll();
        } catch (Exception e) {
            System.out.println("Loi khi cap nhat danh sach khach hang tu CSDL!");
            e.printStackTrace();
        }
    }
}