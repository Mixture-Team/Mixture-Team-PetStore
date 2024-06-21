package hutech.mixture.petstore.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {
    ADMIN(0), // Vai trò quản trị viên, có quyền cao nhất trong hệ thống.
    EMPLOYEE(1),
    USER(2); // Vai trò người dùng bình thường, có quyền hạn giới hạn.
    public final long value; // Biến này lưu giá trị số tương ứng với mỗi vai trò.

}
