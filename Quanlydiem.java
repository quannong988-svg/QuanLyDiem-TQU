package Quanlydiem;
 
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
 
/**
 * =============================================================================
 * HỆ THỐNG QUẢN LÝ ĐIỂM SINH VIÊN TQU - PHIÊN BẢN CHỐNG LỖI NULLPOINTER
 * NGƯỜI THỰC HIỆN: NÔNG VĂN QUÂN
 * QUY MÔ DỰ ÁN: > 1100 LINES
 * =============================================================================
 */
 
public class Quanlydiem extends JFrame {
 
    // --- CÁC BIẾN TOÀN CỤC KHỞI TẠO TẠI CHỖ ĐỂ TRÁNH LỖI NULL ---
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel();
 
    private JTable tableDiem = new JTable();
    private JTable tableServer = new JTable();
    private DefaultTableModel modelDiem = new DefaultTableModel();
    private DefaultTableModel modelServer = new DefaultTableModel();
 
    private JTextField txtSearchName = new JTextField();
    private JTextField txtSearchDate = new JTextField();
    private JTextField txtSearchClass = new JTextField();
    private JTextField txtSearchId = new JTextField();
 
    private JTextField txtHoTen = new JTextField();
    private JTextField txtMaSV = new JTextField();
    private JTextField txtLop = new JTextField();
    private JTextField txtNgaySinh = new JTextField();
    private JTextField txtHocPhan = new JTextField();
    private JTextField txtCC = new JTextField();
    private JTextField txtGK = new JTextField();
    private JTextField txtCK = new JTextField();
 
    private JLabel lblThongKe = new JLabel("Đang tải dữ liệu...");
    private JPanel centerPanel = new JPanel(new CardLayout());
    private JPanel pnlLeft = new JPanel();
    private JPanel studentViewPanel = new JPanel(new BorderLayout());
 
    private boolean daChotDiem = false;
    private String currentRole = "";
    private String currentStudentUser = "";
 
    private JButton btnThem = new JButton();
    private JButton btnXoa = new JButton();
    private JButton btnQLDiem = new JButton();
    private JButton btnServer = new JButton();
    private JButton btnChot = new JButton();
    private JButton btnMo = new JButton();
    private JButton btnExcel = new JButton();
    private JButton btnPhanQuyen = new JButton();
 
    private JLabel lblStudentName = new JLabel();
    private JLabel lblStudentId = new JLabel();
    private JLabel lblStudentClass = new JLabel();
    private JLabel lblStudentBirth = new JLabel();
    private JLabel lblGpa4 = new JLabel();
    private JLabel lblGpa10 = new JLabel();
    private JLabel lblXl = new JLabel();
    private JLabel lblTinChi = new JLabel();
 
    private String[][] monHocK5 = {
        {"Lập trình Java", "3"}, {"Mạng máy tính", "3"}, {"Cơ sở dữ liệu", "3"},
        {"Thiết kế Web", "2"}, {"An toàn thông tin", "3"}, {"Python", "3"},
        {"Trí tuệ nhân tạo", "3"}, {"Phân tích thiết kế HTTT", "3"},
        {"Điện toán đám mây", "2"}, {"Machine Learning", "3"}, {"GDTC 3", "1"}, {"GDANQP", "2"}
    };
 
    private String[][] monHocK6 = {
        {"Python", "3"}, {"Trí tuệ nhân tạo", "3"}, {"Phân tích thiết kế HTTT", "3"},
        {"Điện toán đám mây", "2"}, {"Machine Learning", "3"}, {"GDTC 1", "1"}, {"GDTC 2", "1"}
    };
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Quanlydiem().setVisible(true);
        });
    }
 
    public Quanlydiem() {
        setTitle("HỆ THỐNG QUẢN LÝ ĐIỂM - ĐẠI HỌC TÂN TRÀO");
        setSize(1700, 930);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 700));
 
        mainContainer.setLayout(cardLayout);
        mainContainer.add(createLoginPanel(), "LOGIN");
        mainContainer.add(createMainPanel(), "MAIN");
        add(mainContainer);
 
        cardLayout.show(mainContainer, "LOGIN");
    }
 
    // =========================================================================
    // PANEL ĐĂNG NHẬP
    // =========================================================================
    private JPanel createLoginPanel() {
        JPanel bg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 180, 0), getWidth(), getHeight(), Color.WHITE);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                GradientPaint gp2 = new GradientPaint(0, getHeight(), new Color(255, 255, 0, 120), getWidth(), 0, new Color(0, 120, 255, 120));
                g2.setPaint(gp2);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(null);
 
        JPanel loginBox = new JPanel(null);
        loginBox.setBounds(600, 170, 500, 500);
        loginBox.setBackground(new Color(255, 255, 255, 230));
        loginBox.setBorder(new LineBorder(Color.WHITE, 3, true));
 
        JLabel title = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        title.setFont(new Font("Tahoma", Font.BOLD, 30));
        title.setBounds(20, 40, 450, 40);
 
        JTextField txtUser = new JTextField();
        txtUser.setBorder(new TitledBorder("Username"));
        txtUser.setBounds(70, 140, 350, 60);
 
        JPasswordField txtPass = new JPasswordField();
        txtPass.setBorder(new TitledBorder("Password"));
        txtPass.setBounds(70, 230, 350, 60);
 
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Admin", "Teacher/Faculty", "Student"});
        cbRole.setBounds(70, 320, 350, 45);
 
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setBounds(70, 400, 350, 60);
        btnLogin.setBackground(new Color(0, 102, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Tahoma", Font.BOLD, 22));
 
        btnLogin.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = String.valueOf(txtPass.getPassword());
            String r = cbRole.getSelectedItem().toString();
 
            if (r.equals("Admin")) {
                if (u.equals("admin") && p.equals("123")) {
                    currentRole = "ADMIN"; phanQuyen(); cardLayout.show(mainContainer, "MAIN");
                } else { JOptionPane.showMessageDialog(this, "Sai tài khoản Admin!"); }
            } else if (r.equals("Teacher/Faculty")) {
                if (u.equals("tongtruongdhtt@gmail.tqu") && p.equals("06071995")) {
                    currentRole = "TEACHER"; phanQuyen(); cardLayout.show(mainContainer, "MAIN");
                } else { JOptionPane.showMessageDialog(this, "Sai tài khoản Giáo viên!"); }
            } else {
                checkStudentLogin(u, p);
            }
        });
 
        loginBox.add(title); loginBox.add(txtUser); loginBox.add(txtPass);
        loginBox.add(cbRole); loginBox.add(btnLogin);
        bg.add(loginBox);
        return bg;
    }
 
    private void checkStudentLogin(String u, String p) {
        boolean ok = false;
        for (int i = 0; i < modelDiem.getRowCount(); i++) {
            String m = modelDiem.getValueAt(i, 0).toString();
            String n = modelDiem.getValueAt(i, 2).toString();
            if (u.equals(m) && p.equals(formatPassword(n))) {
                ok = true; currentStudentUser = m; break;
            }
        }
        if (ok) {
            currentRole = "STUDENT"; phanQuyen(); loadStudentPrivateView(); cardLayout.show(mainContainer, "MAIN");
        } else {
            JOptionPane.showMessageDialog(this, "Sai tài khoản Sinh viên!");
        }
    }
 
    // =========================================================================
    // PANEL CHÍNH
    // =========================================================================
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(220, 240, 255));
 
        // ---- Thanh tìm kiếm trên cùng ----
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0, 180, 120));
        top.setBorder(new EmptyBorder(10, 10, 10, 10));
 
        JPanel searchPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        searchPanel.setOpaque(false);
        txtSearchName = createSearchField("🔍 Tìm theo họ tên");
        txtSearchDate = createSearchField("🔍 Tìm theo ngày sinh");
        txtSearchClass = createSearchField("🔍 Tìm theo lớp");
        txtSearchId = createSearchField("🔍 Tìm theo mã SV");
        searchPanel.add(txtSearchName); searchPanel.add(txtSearchDate);
        searchPanel.add(txtSearchClass); searchPanel.add(txtSearchId);
        top.add(searchPanel, BorderLayout.CENTER);
 
        lblThongKe.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblThongKe.setForeground(Color.WHITE);
        top.add(lblThongKe, BorderLayout.SOUTH);
        panel.add(top, BorderLayout.NORTH);
 
        // ---- Sidebar trái ----
        // KHÔNG set preferredSize cứng - BoxLayout tự tính chiều cao thật
        // để JScrollPane biết tổng chiều cao cần scroll
        pnlLeft = new JPanel();
        pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
        pnlLeft.setBorder(new EmptyBorder(15, 15, 15, 15));
        pnlLeft.setBackground(new Color(255, 255, 180));
 
        JLabel sideTitle = new JLabel("BẢNG ĐIỀU KHIỂN");
        sideTitle.setFont(new Font("Tahoma", Font.BOLD, 22));
        sideTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sideTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
 
        initInputFields();
        initControlButtons();
 
        pnlLeft.add(sideTitle);
 
        // --- Form nhập liệu trong JScrollPane để có thể resize ---
        JPanel formPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        formPanel.setOpaque(false);
        formPanel.add(txtHoTen);
        formPanel.add(txtMaSV);
        formPanel.add(txtLop);
        formPanel.add(txtNgaySinh);
        formPanel.add(txtHocPhan);
 
        JPanel diemRow = new JPanel(new GridLayout(1, 3, 5, 0));
        diemRow.setOpaque(false);
        diemRow.add(txtCC);
        diemRow.add(txtGK);
        diemRow.add(txtCK);
 
        formPanel.add(diemRow);
 
        // Nút THÊM + XÓA cạnh nhau
        JPanel rowThemXoa = new JPanel(new GridLayout(1, 2, 8, 0));
        rowThemXoa.setOpaque(false);
        rowThemXoa.add(btnThem);
        rowThemXoa.add(btnXoa);
 
        formPanel.add(rowThemXoa);
        // Không giới hạn chiều cao formPanel - để tự co giãn đúng
        formPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        pnlLeft.add(formPanel);
        pnlLeft.add(Box.createVerticalStrut(10));
 
        // --- Các nút điều khiển khác ---
        JPanel btnPanel = new JPanel(new GridLayout(0, 1, 5, 8));
        btnPanel.setOpaque(false);
        btnPanel.add(btnQLDiem);
        btnPanel.add(btnServer);
        btnPanel.add(btnChot);
        btnPanel.add(btnMo);
        btnPanel.add(btnExcel);
        btnPanel.add(btnPhanQuyen);
 
        JButton btnLogout = createStyledBtn("ĐĂNG XUẤT", Color.RED);
        btnLogout.addActionListener(e -> {
            saveData();  // Lưu dữ liệu tự động khi đăng xuất
            cardLayout.show(mainContainer, "LOGIN");
        });
        btnPanel.add(btnLogout);
 
        pnlLeft.add(btnPanel);
 
        // ---- Bọc pnlLeft trong JScrollPane + 2 nút ▲ ▼ ----
        // Dùng JScrollPane có scrollbar dọc để scroll hoạt động đúng
        JScrollPane leftScroll = new JScrollPane(pnlLeft,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftScroll.setBorder(null);
        // Phải revalidate sau khi show để JScrollPane tính lại preferredSize
        leftScroll.getViewport().setBackground(new Color(255, 255, 180));
 
        // Nút ▲ cuộn lên - dùng timer để cuộn mượt
        JButton btnUp = new JButton("▲");
        btnUp.setBackground(new Color(0, 102, 200));
        btnUp.setForeground(Color.WHITE);
        btnUp.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnUp.setFocusPainted(false);
        btnUp.setPreferredSize(new Dimension(0, 36));
        btnUp.addActionListener(e -> {
            JViewport vp = leftScroll.getViewport();
            Point pt = vp.getViewPosition();
            int newY = Math.max(0, pt.y - 100);
            vp.setViewPosition(new Point(0, newY));
        });
 
        // Nút ▼ cuộn xuống
        JButton btnDown = new JButton("▼");
        btnDown.setBackground(new Color(0, 102, 200));
        btnDown.setForeground(Color.WHITE);
        btnDown.setFont(new Font("Tahoma", Font.BOLD, 18));
        btnDown.setFocusPainted(false);
        btnDown.setPreferredSize(new Dimension(0, 36));
        btnDown.addActionListener(e -> {
            JViewport vp = leftScroll.getViewport();
            Point pt = vp.getViewPosition();
            int maxY = pnlLeft.getPreferredSize().height - vp.getHeight();
            int newY = Math.min(maxY, pt.y + 100);
            vp.setViewPosition(new Point(0, newY));
        });
 
        // Ghép lại: nút ▲ + scrollpane + nút ▼
        JPanel leftWrapper = new JPanel(new BorderLayout());
        leftWrapper.setPreferredSize(new Dimension(440, 0));
        leftWrapper.setBackground(new Color(255, 255, 180));
        leftWrapper.add(btnUp, BorderLayout.NORTH);
        leftWrapper.add(leftScroll, BorderLayout.CENTER);
        leftWrapper.add(btnDown, BorderLayout.SOUTH);
 
        panel.add(leftWrapper, BorderLayout.WEST);
 
        createTablePanels();
        createStudentViewPanel();
        panel.add(centerPanel, BorderLayout.CENTER);
 
        setupSearchLogic();
        initData();
        thongKe();
 
        return panel;
    }
 
    // =========================================================================
    // KHỞI TẠO CÁC TRƯỜNG NHẬP LIỆU
    // =========================================================================
    private void initInputFields() {
        txtHoTen    = createInput("Họ tên sinh viên *");
        txtMaSV     = createInput("Mã sinh viên *");
        txtLop      = createInput("Lớp *");
        txtNgaySinh = createInput("Ngày sinh *");
        txtHocPhan  = createInput("Tên học phần");
        txtCC       = createInput("Chuyên cần (0-10)");
        txtGK       = createInput("Giữa kỳ (0-10)");
        txtCK       = createInput("Cuối kỳ (0-10)");
 
        // Đặt chiều cao tối thiểu cho mỗi trường
        Dimension fieldSize = new Dimension(Integer.MAX_VALUE, 55);
        txtHoTen.setMaximumSize(fieldSize);
        txtMaSV.setMaximumSize(fieldSize);
        txtLop.setMaximumSize(fieldSize);
        txtNgaySinh.setMaximumSize(fieldSize);
        txtHocPhan.setMaximumSize(fieldSize);
        txtCC.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        txtGK.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        txtCK.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
    }
 
    // =========================================================================
    // KHỞI TẠO NÚT BẤM
    // =========================================================================
    private void initControlButtons() {
        btnThem      = createStyledBtn("+ THÊM", new Color(0, 102, 255));
        btnXoa       = createStyledBtn("− XÓA", Color.RED);
        btnQLDiem    = createStyledBtn("QUẢN LÝ ĐIỂM", new Color(0, 102, 255));
        btnServer    = createStyledBtn("QUẢN LÝ MÁY CHỦ", new Color(0, 170, 0));
        btnChot      = createStyledBtn("CHỐT ĐIỂM", new Color(255, 120, 0));
        btnMo        = createStyledBtn("MỞ KHÓA ĐIỂM", new Color(120, 0, 255));
        btnExcel     = createStyledBtn("XUẤT EXCEL/CSV", new Color(0, 140, 140));
        btnPhanQuyen = createStyledBtn("PHÂN QUYỀN GV", new Color(120, 120, 0));
 
        Dimension btnSize = new Dimension(Integer.MAX_VALUE, 42);
        btnThem.setMaximumSize(btnSize);
        btnXoa.setMaximumSize(btnSize);
        btnQLDiem.setMaximumSize(btnSize);
        btnServer.setMaximumSize(btnSize);
        btnChot.setMaximumSize(btnSize);
        btnMo.setMaximumSize(btnSize);
        btnExcel.setMaximumSize(btnSize);
        btnPhanQuyen.setMaximumSize(btnSize);
 
        btnThem.addActionListener(e -> addStudent());
        btnXoa.addActionListener(e -> deleteStudent());
        btnQLDiem.addActionListener(e -> ((CardLayout) centerPanel.getLayout()).show(centerPanel, "DIEM"));
        btnServer.addActionListener(e -> { loadServerData(); ((CardLayout) centerPanel.getLayout()).show(centerPanel, "SERVER"); });
        btnChot.addActionListener(e -> { daChotDiem = true; JOptionPane.showMessageDialog(this, "ĐÃ KHÓA ĐIỂM!"); });
        btnMo.addActionListener(e -> { daChotDiem = false; JOptionPane.showMessageDialog(this, "ĐÃ MỞ KHÓA ĐIỂM!"); });
        btnExcel.addActionListener(e -> exportCSV());
    }
 
    // =========================================================================
    // THÊM SINH VIÊN - ĐÃ SỬA: VALIDATE ĐẦY ĐỦ VÀ THÔNG BÁO RÕ RÀNG
    // =========================================================================
    private void addStudent() {
        if (daChotDiem) {
            JOptionPane.showMessageDialog(this, "Không thể thêm! Điểm đã bị khóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        // Lấy dữ liệu từ các trường
        String hoTen    = txtHoTen.getText().trim();
        String maSV     = txtMaSV.getText().trim();
        String lop      = txtLop.getText().trim();
        String ngaySinh = txtNgaySinh.getText().trim();
        String ccStr    = txtCC.getText().trim();
        String gkStr    = txtGK.getText().trim();
        String ckStr    = txtCK.getText().trim();
 
        // --- Kiểm tra bắt buộc ---
        if (hoTen.isEmpty() || maSV.isEmpty() || lop.isEmpty() || ngaySinh.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Vui lòng nhập đầy đủ:\n• Họ tên\n• Mã sinh viên\n• Lớp\n• Ngày sinh",
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
 
        // --- Kiểm tra mã SV trùng ---
        for (int i = 0; i < modelDiem.getRowCount(); i++) {
            if (modelDiem.getValueAt(i, 0).toString().equals(maSV)) {
                JOptionPane.showMessageDialog(this,
                    "Mã sinh viên \"" + maSV + "\" đã tồn tại trong hệ thống!",
                    "Trùng mã SV", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
 
        // --- Tính điểm nếu có nhập, nếu không để mặc định 0 ---
        double g10 = 0.0;
        if (!ccStr.isEmpty() && !gkStr.isEmpty() && !ckStr.isEmpty()) {
            try {
                double cc = Double.parseDouble(ccStr);
                double gk = Double.parseDouble(gkStr);
                double ck = Double.parseDouble(ckStr);
 
                if (cc < 0 || cc > 10 || gk < 0 || gk > 10 || ck < 0 || ck > 10) {
                    JOptionPane.showMessageDialog(this,
                        "Điểm phải nằm trong khoảng 0 - 10!",
                        "Điểm không hợp lệ", JOptionPane.WARNING_MESSAGE);
                    return;
                }
 
                g10 = Math.round(((cc * 0.1 + gk * 0.3 + ck * 0.6)) * 10) / 10.0;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Điểm CC, GK, CK phải là số hợp lệ (vd: 7.5)!",
                    "Lỗi định dạng", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
 
        // --- Thêm vào bảng ---
        modelDiem.addRow(new Object[]{
            maSV, hoTen, ngaySinh, lop,
            g10, convert4(g10), xepLoai(convert4(g10)), "XEM CHI TIẾT"
        });
 
        thongKe();
        clearFields();
 
        JOptionPane.showMessageDialog(this,
            "Đã thêm sinh viên: " + hoTen + " (" + maSV + ") thành công!",
            "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
 
    private void deleteStudent() {
        int r = tableDiem.getSelectedRow();
        if (r != -1) {
            int modelRow = tableDiem.convertRowIndexToModel(r);
            String ten = modelDiem.getValueAt(modelRow, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận xóa sinh viên: " + ten + "?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                modelDiem.removeRow(modelRow);
                thongKe();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên trong bảng để xóa!", "Chưa chọn", JOptionPane.WARNING_MESSAGE);
        }
    }
 
    private void clearFields() {
        txtHoTen.setText(""); txtMaSV.setText(""); txtLop.setText("");
        txtNgaySinh.setText(""); txtHocPhan.setText("");
        txtCC.setText(""); txtGK.setText(""); txtCK.setText("");
    }
 
    // =========================================================================
    // PANEL XEM ĐIỂM SINH VIÊN
    // =========================================================================
    private void createStudentViewPanel() {
        studentViewPanel = new JPanel(new BorderLayout());
        studentViewPanel.setBackground(Color.WHITE);
 
        JPanel topInfo = new JPanel(new GridLayout(6, 2, 10, 10));
        topInfo.setBorder(new EmptyBorder(20, 20, 20, 20));
        topInfo.setBackground(new Color(245, 245, 245));
 
        lblStudentName  = createBoldLabel("Họ tên: ");
        lblStudentId    = createBoldLabel("Mã SV: ");
        lblStudentClass = createBoldLabel("Lớp: ");
        lblStudentBirth = createBoldLabel("Ngày sinh: ");
        lblGpa4         = createBoldLabel("TBC tích lũy hệ 4: ");
        lblGpa10        = createBoldLabel("TBC học tập hệ 10: ");
        lblXl           = createBoldLabel("Xếp loại: ");
        lblTinChi       = createBoldLabel("Số tín chỉ tích lũy: ");
 
        topInfo.add(lblStudentName); topInfo.add(lblGpa4);
        topInfo.add(lblStudentId);  topInfo.add(lblXl);
        topInfo.add(lblStudentClass); topInfo.add(lblTinChi);
        topInfo.add(lblStudentBirth); topInfo.add(lblGpa10);
        topInfo.add(new JLabel("Số môn thi lại: 0")); topInfo.add(new JLabel("Số môn học lại: 0"));
        topInfo.add(new JLabel("Số môn chờ điểm: 0")); topInfo.add(new JLabel(""));
 
        studentViewPanel.add(topInfo, BorderLayout.NORTH);
 
        String[] cols = {"Học kỳ", "Năm học", "Mã HP", "Tên học phần", "Số tín chỉ", "Thang 10", "Thang 4", "Điểm chữ", "Xem Chi Tiết"};
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        JTable stTable = new JTable(m);
        stTable.setRowHeight(35);
        centerTable(stTable);
 
        stTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = stTable.rowAtPoint(e.getPoint());
                int c = stTable.columnAtPoint(e.getPoint());
                if (c == 8 && r >= 0) {
                    showCompDetail(stTable.getValueAt(r, 3).toString(), stTable.getValueAt(r, 5).toString());
                }
            }
        });
 
        studentViewPanel.add(new JScrollPane(stTable), BorderLayout.CENTER);
        centerPanel.add(studentViewPanel, "STUDENT_VIEW");
    }
 
    private void showCompDetail(String m, String d) {
        double val = Double.parseDouble(d.toString());
        String info = "MÔN: " + m.toUpperCase() + "\nĐiểm CC: " + val + "\nĐiểm GK: " + val + "\nĐiểm CK: " + val + "\n\nTỔNG KẾT: " + d;
        JOptionPane.showMessageDialog(this, info, "CHI TIẾT ĐIỂM", JOptionPane.INFORMATION_MESSAGE);
    }
 
    private void loadStudentPrivateView() {
        for (int i = 0; i < modelDiem.getRowCount(); i++) {
            if (modelDiem.getValueAt(i, 0).toString().equals(currentStudentUser)) {
                lblStudentName.setText("Họ tên: " + modelDiem.getValueAt(i, 1));
                lblStudentId.setText("Mã SV: " + currentStudentUser);
                lblStudentClass.setText("Lớp: " + modelDiem.getValueAt(i, 3));
                lblStudentBirth.setText("Ngày sinh: " + modelDiem.getValueAt(i, 2));
                lblGpa4.setText("TBC tích lũy hệ 4: " + modelDiem.getValueAt(i, 5));
                lblGpa10.setText("TBC học tập hệ 10: " + modelDiem.getValueAt(i, 4));
                lblXl.setText("Xếp loại: " + modelDiem.getValueAt(i, 6));
 
                DefaultTableModel m = (DefaultTableModel)
                    ((JTable) ((JScrollPane) studentViewPanel.getComponent(1)).getViewport().getView()).getModel();
                m.setRowCount(0);
                Random r = new Random();
                String[][] mon = modelDiem.getValueAt(i, 3).toString().equals("CNTT_K5") ? monHocK5 : monHocK6;
                int sumTc = 0;
                for (String[] s : mon) {
                    double d10 = 5 + r.nextDouble() * 5;
                    d10 = Math.round(d10 * 10) / 10.0;
                    sumTc += Integer.parseInt(s[1]);
                    m.addRow(new Object[]{"2", "2025-2026", "TQU" + r.nextInt(100), s[0], s[1], d10, convert4(d10), convertChar(d10), "Xem chi tiết"});
                }
                lblTinChi.setText("Số tín chỉ tích lũy: " + sumTc);
                break;
            }
        }
        ((CardLayout) centerPanel.getLayout()).show(centerPanel, "STUDENT_VIEW");
    }
 
    // =========================================================================
    // PHÂN QUYỀN
    // =========================================================================
    private void phanQuyen() {
        boolean staff = currentRole.equals("ADMIN") || currentRole.equals("TEACHER");
        pnlLeft.setVisible(true);
        for (Component c : pnlLeft.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().equals("ĐĂNG XUẤT")) continue;
            c.setVisible(staff);
        }
        btnServer.setVisible(currentRole.equals("ADMIN"));
        btnPhanQuyen.setVisible(currentRole.equals("ADMIN"));
        if (staff) ((CardLayout) centerPanel.getLayout()).show(centerPanel, "DIEM");
    }
 
    // =========================================================================
    // TẠO BẢNG ĐIỂM VÀ MÁY CHỦ
    // =========================================================================
    private void createTablePanels() {
        // Bảng điểm
        JPanel pnlDiem = new JPanel(new BorderLayout());
        JLabel l = new JLabel("QUẢN LÝ ĐIỂM SINH VIÊN", 0);
        l.setFont(new Font("Tahoma", Font.BOLD, 30));
        pnlDiem.add(l, BorderLayout.NORTH);
 
        String[] cols = {"Mã SV", "Họ tên", "Ngày sinh", "Lớp", "GPA Hệ 10", "GPA Hệ 4", "Xếp loại", "Thao Tác"};
        modelDiem = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableDiem = new JTable(modelDiem);
        tableDiem.setFocusable(false);
        tableDiem.setRowHeight(40);
        centerTable(tableDiem);
        pnlDiem.add(new JScrollPane(tableDiem), BorderLayout.CENTER);
        centerPanel.add(pnlDiem, "DIEM");
 
        // Bảng máy chủ
        JPanel pnlServer = new JPanel(new BorderLayout());
        JLabel l2 = new JLabel("QUẢN LÝ MÁY CHỦ", 0);
        l2.setFont(new Font("Tahoma", Font.BOLD, 30));
        pnlServer.add(l2, BorderLayout.NORTH);
        String[] cols2 = {"Mã SV", "Họ tên", "Lớp", "Ngày sinh", "Username", "Password"};
        modelServer = new DefaultTableModel(cols2, 0);
        tableServer = new JTable(modelServer);
        tableServer.setRowHeight(40);
        centerTable(tableServer);
        pnlServer.add(new JScrollPane(tableServer), BorderLayout.CENTER);
        centerPanel.add(pnlServer, "SERVER");
 
        // Click xem chi tiết
        tableDiem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = tableDiem.rowAtPoint(e.getPoint());
                if (r >= 0 && tableDiem.columnAtPoint(e.getPoint()) == 7) {
                    int modelRow = tableDiem.convertRowIndexToModel(r);
                    showAdmDetail(
                        modelDiem.getValueAt(modelRow, 0).toString(),
                        modelDiem.getValueAt(modelRow, 1).toString(),
                        modelDiem.getValueAt(modelRow, 3).toString()
                    );
                }
            }
        });
    }
 
    private void showAdmDetail(String msv, String ten, String lop) {
        JDialog d = new JDialog(this, "CHI TIẾT: " + ten, true);
        d.setSize(1100, 600);
        String[] h = {"Môn học", "Tín chỉ", "CC", "GK", "CK", "Hệ 10", "Hệ 4", "Chữ"};
        DefaultTableModel m = new DefaultTableModel(h, 0);
        JTable tb = new JTable(m);
        tb.setRowHeight(35);
        centerTable(tb);
        Random r = new Random();
        String[][] mon = lop.equals("CNTT_K5") ? monHocK5 : monHocK6;
        for (String[] s : mon) {
            double v = 5 + r.nextDouble() * 5;
            v = Math.round(v * 10) / 10.0;
            m.addRow(new Object[]{s[0], s[1], v, v, v, v, convert4(v), convertChar(v)});
        }
        d.add(new JScrollPane(tb));
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }
 
    // =========================================================================
    // TÌM KIẾM
    // =========================================================================
    private void setupSearchLogic() {
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { runSearch(); }
            public void removeUpdate(DocumentEvent e) { runSearch(); }
            public void changedUpdate(DocumentEvent e) { runSearch(); }
        };
        txtSearchName.getDocument().addDocumentListener(dl);
        txtSearchDate.getDocument().addDocumentListener(dl);
        txtSearchClass.getDocument().addDocumentListener(dl);
        txtSearchId.getDocument().addDocumentListener(dl);
    }
 
    private void runSearch() {
        String n = txtSearchName.getText().toLowerCase();
        String d = txtSearchDate.getText().toLowerCase();
        String c = txtSearchClass.getText().toLowerCase();
        String i = txtSearchId.getText().toLowerCase();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelDiem);
        tableDiem.setRowSorter(sorter);
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> e) {
                return e.getStringValue(1).toLowerCase().contains(n)
                    && e.getStringValue(2).toLowerCase().contains(d)
                    && e.getStringValue(3).toLowerCase().contains(c)
                    && e.getStringValue(0).toLowerCase().contains(i);
            }
        });
    }
 
    // =========================================================================
    // DỮ LIỆU MẪU + THỐNG KÊ + XUẤT FILE
    // =========================================================================
    private void loadServerData() {
        modelServer.setRowCount(0);
        for (int i = 0; i < modelDiem.getRowCount(); i++) {
            String m = modelDiem.getValueAt(i, 0).toString();
            String ns = modelDiem.getValueAt(i, 2).toString();
            modelServer.addRow(new Object[]{m, modelDiem.getValueAt(i, 1), modelDiem.getValueAt(i, 3), ns, m, formatPassword(ns)});
        }
    }
 
    private String formatPassword(String ns) {
        String[] p = ns.split("/");
        if (p.length != 3) return ns;
        return (p[0].length() == 1 ? "0" + p[0] : p[0])
             + (p[1].length() == 1 ? "0" + p[1] : p[1])
             + p[2];
    }
 
    private void exportCSV() {
        try (FileWriter fw = new FileWriter("Xuat_Diem.csv")) {
            for (int i = 0; i < modelDiem.getColumnCount(); i++)
                fw.write(modelDiem.getColumnName(i) + ",");
            fw.write("\n");
            for (int i = 0; i < modelDiem.getRowCount(); i++) {
                for (int j = 0; j < modelDiem.getColumnCount(); j++)
                    fw.write(modelDiem.getValueAt(i, j).toString() + ",");
                fw.write("\n");
            }
            JOptionPane.showMessageDialog(this, "Xuất file Xuat_Diem.csv thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất file: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
 
    // Tên file lưu dữ liệu
    private static final String DATA_FILE = "QuanLyDiem_Data.csv";
 
    /**
     * Lưu toàn bộ dữ liệu bảng điểm ra file CSV (tự động khi đăng xuất)
     */
    private void saveData() {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(DATA_FILE), "UTF-8"))) {
            // Dòng đầu: header
            bw.write("MaSV|HoTen|NgaySinh|Lop|GPA10|GPA4|XepLoai");
            bw.newLine();
            for (int i = 0; i < modelDiem.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                // Lưu 7 cột đầu (bỏ cột "Thao Tác")
                for (int j = 0; j < 7; j++) {
                    sb.append(modelDiem.getValueAt(i, j).toString());
                    if (j < 6) sb.append("|");
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    /**
     * Đọc dữ liệu từ file CSV.
     * Trả về true nếu đọc thành công, false nếu file không tồn tại.
     */
    private boolean loadData() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return false;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(f), "UTF-8"))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // bỏ header
                if (line.trim().isEmpty()) continue;
                String[] p = line.split("\\|", -1);
                if (p.length < 7) continue;
                modelDiem.addRow(new Object[]{
                    p[0], p[1], p[2], p[3],
                    Double.parseDouble(p[4]),
                    Double.parseDouble(p[5]),
                    p[6],
                    "XEM CHI TIẾT"
                });
            }
            return modelDiem.getRowCount() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
 
    private void initData() {
        // Ưu tiên đọc dữ liệu đã lưu
        if (loadData()) return;
 
        // Không có file -> sinh dữ liệu mẫu lần đầu
        String[] h = {"Nguyễn", "Trần", "Lê", "Phạm"};
        String[] dem = {"Văn", "Thị", "Minh"};
        String[] t = {"Quân", "Lan", "Hùng", "Duy"};
        Random r = new Random();
        for (int i = 1; i <= 100; i++) {
            String ht = h[r.nextInt(4)] + " " + dem[r.nextInt(3)] + " " + t[r.nextInt(4)];
            String m = "245480" + String.format("%04d", i);
            String ns = (1 + r.nextInt(28)) + "/" + (1 + r.nextInt(12)) + (i <= 50 ? "/2006" : "/2007");
            double g = Math.round((5 + r.nextDouble() * 5) * 10) / 10.0;
            modelDiem.addRow(new Object[]{m, ht, ns, (i <= 50 ? "CNTT_K5" : "CNTT_K6"), g, convert4(g), xepLoai(convert4(g)), "XEM CHI TIẾT"});
        }
        // Lưu luôn dữ liệu mẫu để lần sau đọc được
        saveData();
    }
 
    private void thongKe() {
        int xs = 0, g = 0, k = 0, tb = 0, y = 0;
        for (int i = 0; i < modelDiem.getRowCount(); i++) {
            String xl = modelDiem.getValueAt(i, 6).toString();
            if (xl.equals("Xuất sắc")) xs++;
            else if (xl.equals("Giỏi")) g++;
            else if (xl.equals("Khá")) k++;
            else if (xl.equals("Trung bình")) tb++;
            else y++;
        }
        lblThongKe.setText("Xuất sắc: " + xs + " | Giỏi: " + g + " | Khá: " + k + " | TB: " + tb + " | Yếu: " + y);
    }
 
    // =========================================================================
    // HELPER METHODS
    // =========================================================================
    private JTextField createInput(String t) {
        JTextField f = new JTextField();
        f.setBorder(new TitledBorder(t));
        f.setFont(new Font("Tahoma", Font.PLAIN, 14));
        f.setPreferredSize(new Dimension(0, 55));
        return f;
    }
 
    private JTextField createSearchField(String t) {
        JTextField f = new JTextField();
        f.setBorder(new TitledBorder(t));
        return f;
    }
 
    private JButton createStyledBtn(String t, Color c) {
        JButton b = new JButton(t);
        b.setBackground(c);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Tahoma", Font.BOLD, 15));
        b.setFocusPainted(false);
        return b;
    }
 
    private JLabel createBoldLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Tahoma", Font.BOLD, 16));
        return l;
    }
 
    private void centerTable(JTable tb) {
        DefaultTableCellRenderer c = new DefaultTableCellRenderer();
        c.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tb.getColumnCount(); i++)
            tb.getColumnModel().getColumn(i).setCellRenderer(c);
    }
 
    // Quy đổi hệ 10 -> hệ 4 (theo chuẩn Bộ GD&ĐT)
    private double convert4(double d) {
        if (d >= 8.5) return 4.0;
        if (d >= 8.0) return 3.5;
        if (d >= 7.0) return 3.0;
        if (d >= 6.5) return 2.5;
        if (d >= 5.5) return 2.0;
        if (d >= 5.0) return 1.5;
        if (d >= 4.0) return 1.0;
        return 0.0;
    }
 
    // Quy đổi hệ 10 -> điểm chữ (theo chuẩn Bộ GD&ĐT)
    private String convertChar(double d) {
        if (d >= 8.5) return "A";
        if (d >= 8.0) return "B+";
        if (d >= 7.0) return "B";
        if (d >= 6.5) return "C+";
        if (d >= 5.5) return "C";
        if (d >= 5.0) return "D+";
        if (d >= 4.0) return "D";
        return "F";
    }
 
    // Xếp loại theo điểm hệ 4 (theo chuẩn Bộ GD&ĐT)
    private String xepLoai(double g) {
        if (g >= 3.6) return "Xuất sắc";   // 3.6 - 4.0
        if (g >= 3.2) return "Giỏi";        // 3.2 - 3.59
        if (g >= 2.5) return "Khá";          // 2.5 - 3.19
        if (g >= 2.0) return "Trung bình";   // 2.0 - 2.49
        return "Yếu";                         // < 2.0
    }
}