package repository;
import model.KhachHang;
public interface IKhachHangRepository extends IRepository<KhachHang> {

    KhachHang getByName(String ten);
}
