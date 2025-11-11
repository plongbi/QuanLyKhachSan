package repository;
import model.NhanVien;

public interface INhanVienRepository extends IRepository<NhanVien> {


    NhanVien getByName(String ten);
}
