package nguyentrinhan70.example.com.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class NhanVienUI extends JFrame {
	JComboBox<String> cboPhongBan;
	Connection conn;
	PreparedStatement preparedStatement;
	
	JList<String> listNhanVien;
	
	JTextField txtMa, txtTen;
	JButton btnLuuNhanVien;
	
	String mapb;
	public NhanVienUI(String title){
		super(title);
		addControls();
		addEvents();
		ketNoiDuLieu();
		hienThiDanhSachPhongBan();
	}

	private void hienThiDanhSachPhongBan() {
		// TODO Auto-generated method stub
		try{
			
			preparedStatement = conn.prepareStatement("Select * from PhongBan" );
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()){
				String pb = resultSet.getString("maPhongBan") + "-" + resultSet.getString("TenPhongBan");
				cboPhongBan.addItem(pb);
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}

	private void ketNoiDuLieu() {
		// TODO Auto-generated method stub
		try{
			
			String dataBase = "csdl/dbSinhVien.accdb";
			String strConn = "jdbc:ucanaccess://" + dataBase;
			conn = DriverManager.getConnection(strConn);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private void addEvents() {
		// TODO Auto-generated method stub
		cboPhongBan.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(cboPhongBan.getSelectedIndex()==-1) return;
				mapb =  cboPhongBan.getSelectedItem().toString().split("-")[0];
				xemDanhSachNhanVienTheoPhongBan(mapb);
			}
		});
		
		btnLuuNhanVien.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				xuLyLuuNhanVien();
			}
		});
		
	}
	public boolean kiemTraMaNVTonTai(String maNV) {
		try{
			String sql = "Select * from SinhVien where ma  = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, maNV);
			ResultSet resultSet = preparedStatement.executeQuery();
			return resultSet.next();
			
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return false;
	}

	protected void xuLyLuuNhanVien() {
		// TODO Auto-generated method stub
		if(kiemTraMaNVTonTai(txtMa.getText())==false){

			try
			{
				String sql= "insert into SinhVien(Ma,Ten,MaPhongBan) values(?,?,?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, txtMa.getText());
				preparedStatement.setString(2, txtTen.getText());
				preparedStatement.setString(3, mapb);
				int x = preparedStatement.executeUpdate();
				if(x>0){
					xemDanhSachNhanVienTheoPhongBan(mapb);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		else
		{
			try{
				String sql = "Update SinhVien set ten=? where ma=?";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, txtTen.getText());
				preparedStatement.setString(2, txtMa.getText());
				int x = preparedStatement.executeUpdate();
				if(x>0){
					xemDanhSachNhanVienTheoPhongBan(mapb);
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}

	protected void xemDanhSachNhanVienTheoPhongBan(String mapb) {
		// TODO Auto-generated method stub
		try{
			String sql = "Select * from SinhVien where maPhongban = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, mapb);//Gán giá trị cho ?
			ResultSet resultSet = preparedStatement.executeQuery();
			Vector<String> vec = new Vector<>();
			while(resultSet.next()){
				vec.add(resultSet.getString("ma") + "-" + resultSet.getString("ten"));
			}
			listNhanVien.setListData(vec);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
	}

	private void addControls() {
		// TODO Auto-generated method stub
		Container con = getContentPane();
		con.setLayout(new BoxLayout(con, BoxLayout.Y_AXIS));
		JPanel pnPhongBan = new JPanel();
		pnPhongBan.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblPhongBan = new JLabel("Chọn phòng ban");
		cboPhongBan = new JComboBox<>();
		cboPhongBan.setPreferredSize(new Dimension(300, 30));
		pnPhongBan.add(lblPhongBan);
		pnPhongBan.add(cboPhongBan);
		con.add(pnPhongBan);
				
		JPanel pnNhanVien  = new JPanel();
		pnNhanVien.setLayout(new BorderLayout());
		listNhanVien = new JList<>();
		JScrollPane sc  = new JScrollPane(listNhanVien,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		pnNhanVien.add(sc,BorderLayout.CENTER);
		con.add(pnNhanVien);
				
		JPanel pnChiTiet = new JPanel();
		pnChiTiet.setLayout(new BoxLayout(pnChiTiet, BoxLayout.Y_AXIS));
		con.add(pnChiTiet, BorderLayout.SOUTH);
		
		JPanel pnMa = new JPanel();
		pnMa.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMa = new JLabel("Mã:");
		txtMa = new JTextField(15);
		pnMa.add(lblMa);
		pnMa.add(txtMa);
		
		JPanel pnTen = new JPanel();
		pnTen.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblTen = new JLabel("Tên:");
		txtTen = new JTextField(15);
		pnMa.add(lblTen);
		pnMa.add(txtTen);
		
		pnChiTiet.add(pnMa);
		pnChiTiet.add(pnTen);
		
		JPanel pnButton = new JPanel();
		pnButton.setLayout(new FlowLayout(FlowLayout.LEFT));
		btnLuuNhanVien = new JButton("Lưu sinh viên");
		pnButton.add(btnLuuNhanVien);
		pnChiTiet.add(pnButton);
		
		
		
	}
	public void showWindow(){
		this.setSize(500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

}

