import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.*;

public class SistemTambahAnggotaOSIS extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JTextField txtNIS, txtNama, txtKelas, txtCariData;
    private JComboBox<String> cmbJurusan;
    private JButton btnTambah, btnUpdate, btnHapus, btnReset;

    private JTable tabelSiswa, tabelHistory;
    private DefaultTableModel modelTabel, modelHistory;
    private TableRowSorter<DefaultTableModel> sorter;

    private final ArrayList<Siswa> dataSiswa;
    private final ArrayList<HistoryEntry> dataHistory;
    private int selectedRow = -1;

    private static final String DATA_FILE = "data_siswa.txt";
    private static final String HISTORY_FILE = "history_transaksi.txt";

    private JLabel lblTotalSiswa, lblTotalIPA, lblTotalIPS, lblTotalBahasa;

    private final Color COLOR_PRIMARY = new Color(142, 68, 173);
    private final Color COLOR_SECONDARY = new Color(52, 152, 219);
    private final Color COLOR_SUCCESS = new Color(34, 139, 34);
    private final Color COLOR_WARNING = new Color(255, 140, 0);
    private final Color COLOR_DANGER = new Color(220, 20, 60);
    private final Color COLOR_INFO = new Color(70, 130, 180);
    private final Color COLOR_BG = new Color(240, 248, 255);

    public SistemTambahAnggotaOSIS() {
        dataSiswa = new ArrayList<>();
        dataHistory = new ArrayList<>();
        muatDataDariFile();
        muatHistoryDariFile();
        initComponents();
        setTitle("Sistem Penghitungan Jumlah Anggota OSIS");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                simpanDataKeFile();
                simpanHistoryKeFile();
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        add(createHeaderPanel(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(COLOR_BG);

        mainPanel.add(createDashboardPage(), "DASHBOARD");
        mainPanel.add(createListDataPage(), "LIST_DATA");
        mainPanel.add(createFormPage(), "FORM");
        mainPanel.add(createHistoryPage(), "HISTORY");

        add(mainPanel, BorderLayout.CENTER);

        cardLayout.show(mainPanel, "DASHBOARD");
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 100));

        JLabel lblJudul = new JLabel("SISTEM PENGHITUNGAN JUMLAH ANGGOTA OSIS", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setBorder(BorderFactory.createEmptyBorder(15, 0, 8, 0));
        headerPanel.add(lblJudul, BorderLayout.NORTH);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        navPanel.setBackground(COLOR_PRIMARY);

        JButton btnDashboard = createNavButton("🏠 Dashboard");
        JButton btnListData = createNavButton("📋 List Data");
        JButton btnForm = createNavButton("➕ Input Data");
        JButton btnHistory = createNavButton("📊 History");

        btnDashboard.addActionListener(e -> {
            updateStatistik();
            cardLayout.show(mainPanel, "DASHBOARD");
        });

        btnListData.addActionListener(e -> {
            updateTabel();
            cardLayout.show(mainPanel, "LIST_DATA");
        });

        btnForm.addActionListener(e -> {
            resetForm();
            cardLayout.show(mainPanel, "FORM");
        });

        btnHistory.addActionListener(e -> {
            updateHistoryTable();
            cardLayout.show(mainPanel, "HISTORY");
        });

        navPanel.add(btnDashboard);
        navPanel.add(btnListData);
        navPanel.add(btnForm);
        navPanel.add(btnHistory);

        headerPanel.add(navPanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(COLOR_SECONDARY);
        button.setForeground(Color.black);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(COLOR_SECONDARY);
            }
        });

        return button;
    }

    private JPanel createDashboardPage() {
        JPanel dashboardPage = new JPanel(new BorderLayout(15, 15));
        dashboardPage.setBackground(COLOR_BG);
        dashboardPage.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblPageTitle = new JLabel("🏠 DASHBOARD - HALAMAN UTAMA");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPageTitle.setForeground(COLOR_PRIMARY);
        lblPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblPageTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        dashboardPage.add(lblPageTitle, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(COLOR_BG);

        JPanel statsPanel = createStatisticsPanel();
        centerPanel.add(statsPanel);

        dashboardPage.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(COLOR_BG);
        JLabel lblInfo = new JLabel("Pilih menu di atas untuk mulai mengelola data anggota OSIS");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblInfo.setForeground(Color.GRAY);
        bottomPanel.add(lblInfo);
        dashboardPage.add(bottomPanel, BorderLayout.SOUTH);

        return dashboardPage;
    }

    private JPanel createStatisticsPanel() {
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setPreferredSize(new Dimension(600, 320));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 3, true),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        statsPanel.add(createStatCard("Total Siswa", "0", COLOR_PRIMARY, "👥"));
        statsPanel.add(createStatCard("Jurusan IPA", "0", COLOR_SUCCESS, "🔬"));
        statsPanel.add(createStatCard("Jurusan IPS", "0", COLOR_WARNING, "📚"));
        statsPanel.add(createStatCard("Jurusan Bahasa", "0", COLOR_INFO, "🌍"));

        return statsPanel;
    }

    private JPanel createStatCard(String title, String value, Color color, String emoji) {
        JPanel card = new JPanel(new BorderLayout(8, 8));
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblEmoji = new JLabel(emoji, SwingConstants.CENTER);
        lblEmoji.setFont(new Font("Segoe UI", Font.PLAIN, 40));
        card.add(lblEmoji, BorderLayout.NORTH);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValue.setForeground(Color.WHITE);

        if (title.equals("Total Siswa")) lblTotalSiswa = lblValue;
        else if (title.equals("Jurusan IPA")) lblTotalIPA = lblValue;
        else if (title.equals("Jurusan IPS")) lblTotalIPS = lblValue;
        else if (title.equals("Jurusan Bahasa")) lblTotalBahasa = lblValue;

        card.add(lblValue, BorderLayout.CENTER);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);
        card.add(lblTitle, BorderLayout.SOUTH);

        return card;
    }

    private void updateStatistik() {
        int totalIPA = 0, totalIPS = 0, totalBahasa = 0;

        for (Siswa siswa : dataSiswa) {
            switch (siswa.getJurusan()) {
                case "IPA": totalIPA++; break;
                case "IPS": totalIPS++; break;
                case "Bahasa": totalBahasa++; break;
            }
        }

        if (lblTotalSiswa != null) lblTotalSiswa.setText(String.valueOf(dataSiswa.size()));
        if (lblTotalIPA != null) lblTotalIPA.setText(String.valueOf(totalIPA));
        if (lblTotalIPS != null) lblTotalIPS.setText(String.valueOf(totalIPS));
        if (lblTotalBahasa != null) lblTotalBahasa.setText(String.valueOf(totalBahasa));
    }

    private JPanel createListDataPage() {
        JPanel listDataPage = new JPanel(new BorderLayout(10, 10));
        listDataPage.setBackground(COLOR_BG);
        listDataPage.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblPageTitle = new JLabel("📋 HALAMAN LIST DATA SISWA");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPageTitle.setForeground(COLOR_PRIMARY);
        lblPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblPageTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 12, 0));
        listDataPage.add(lblPageTitle, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        listDataPage.add(tablePanel, BorderLayout.CENTER);

        return listDataPage;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.white);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 3, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);

        JLabel lblCari = new JLabel("🔍 Cari Data:");
        lblCari.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchPanel.add(lblCari);

        txtCariData = new JTextField(25);
        txtCariData.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtCariData.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtCariData.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                cariData(txtCariData.getText());
            }
        });
        searchPanel.add(txtCariData);

        tablePanel.add(searchPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"No", "NIS", "Nama Siswa", "Kelas", "Jurusan"};
        modelTabel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelSiswa = new JTable(modelTabel);
        tabelSiswa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabelSiswa.setRowHeight(30);
        tabelSiswa.setSelectionBackground(new Color(173, 216, 230));
        tabelSiswa.setSelectionForeground(Color.black);
        tabelSiswa.setGridColor(new Color(200, 200, 200));
        tabelSiswa.setShowGrid(true);

        // Table header styling
        JTableHeader header = tabelSiswa.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(COLOR_PRIMARY);
        header.setForeground(Color.black);
        header.setPreferredSize(new Dimension(0, 40));

        // Enable sorting
        sorter = new TableRowSorter<>(modelTabel);
        tabelSiswa.setRowSorter(sorter);

        tabelSiswa.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int viewRow = tabelSiswa.getSelectedRow();
                if (viewRow >= 0) {
                    selectedRow = tabelSiswa.convertRowIndexToModel(viewRow);
                    if (selectedRow >= 0) {
                        tampilkanDataKeForm(selectedRow);
                        cardLayout.show(mainPanel, "FORM");
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelSiswa);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel lblInfo = new JLabel("💡 Tips: Klik header kolom untuk sorting | Klik baris untuk edit data");
        lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblInfo.setForeground(Color.GRAY);
        infoPanel.add(lblInfo, BorderLayout.CENTER);

        tablePanel.add(infoPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private void cariData(String keyword) {
        if (keyword.trim().isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + keyword));
        }
    }

    private JPanel createFormPage() {
        JPanel formPage = new JPanel(new BorderLayout(10, 10));
        formPage.setBackground(COLOR_BG);
        formPage.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblPageTitle = new JLabel("➕ HALAMAN FORM INPUT DATA SISWA");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPageTitle.setForeground(COLOR_PRIMARY);
        lblPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblPageTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        formPage.add(lblPageTitle, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(COLOR_BG);
        centerWrapper.add(createFormPanel());
        formPage.add(centerWrapper, BorderLayout.CENTER);

        return formPage;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setPreferredSize(new Dimension(480, 420));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 3, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblFormTitle = new JLabel("📝 Form Input Data Siswa");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblFormTitle.setForeground(COLOR_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(lblFormTitle, gbc);

        gbc.gridwidth = 1;

        // NIS
        JLabel lblNIS = new JLabel("NIS:");
        lblNIS.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(lblNIS, gbc);

        txtNIS = new JTextField(18);
        txtNIS.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNIS.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridx = 1;
        formPanel.add(txtNIS, gbc);

        // Nama
        JLabel lblNama = new JLabel("Nama Siswa:");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lblNama, gbc);

        txtNama = new JTextField(18);
        txtNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNama.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridx = 1;
        formPanel.add(txtNama, gbc);

        // Kelas
        JLabel lblKelas = new JLabel("Kelas:");
        lblKelas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(lblKelas, gbc);

        txtKelas = new JTextField(18);
        txtKelas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtKelas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        gbc.gridx = 1;
        formPanel.add(txtKelas, gbc);

        // Jurusan
        JLabel lblJurusan = new JLabel("Jurusan:");
        lblJurusan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(lblJurusan, gbc);

        String[] jurusan = {"IPA", "IPS", "Bahasa"};
        cmbJurusan = new JComboBox<>(jurusan);
        cmbJurusan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(cmbJurusan, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 12, 12));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        btnTambah = createStyledButton("Tambah", COLOR_SUCCESS);
        btnUpdate = createStyledButton("Update", COLOR_WARNING);
        btnHapus = createStyledButton("Hapus", COLOR_DANGER);
        btnReset = createStyledButton("Reset", COLOR_INFO);

        buttonPanel.add(btnTambah);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnReset);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        formPanel.add(buttonPanel, gbc);

        btnTambah.addActionListener(e -> tambahData());
        btnUpdate.addActionListener(e -> updateData());
        btnHapus.addActionListener(e -> hapusData());
        btnReset.addActionListener(e -> resetForm());

        return formPanel;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.setColor(getForeground());
                g2.drawString(getText(), (getWidth() - stringWidth) / 2,
                        (getHeight() + stringHeight) / 2 - 2);
                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(100, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JPanel createHistoryPage() {
        JPanel historyPage = new JPanel(new BorderLayout(10, 10));
        historyPage.setBackground(COLOR_BG);
        historyPage.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblPageTitle = new JLabel("📊 HALAMAN HISTORY & LAPORAN TRANSAKSI");
        lblPageTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPageTitle.setForeground(COLOR_PRIMARY);
        lblPageTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblPageTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 12, 0));
        historyPage.add(lblPageTitle, BorderLayout.NORTH);

        JPanel historyTablePanel = createHistoryTablePanel();
        historyPage.add(historyTablePanel, BorderLayout.CENTER);

        return historyPage;
    }

    private JPanel createHistoryTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout(10, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 3, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] columnNames = {"No", "Tanggal & Waktu", "Aksi", "NIS", "Nama Siswa", "Detail"};
        modelHistory = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelHistory = new JTable(modelHistory);
        tabelHistory.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelHistory.setRowHeight(30);
        tabelHistory.setSelectionBackground(new Color(173, 216, 230));
        tabelHistory.setSelectionForeground(Color.BLACK);
        tabelHistory.setGridColor(new Color(200, 200, 200));
        tabelHistory.setShowGrid(true);

        JTableHeader header = tabelHistory.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(COLOR_PRIMARY);
        header.setForeground(Color.black);
        header.setPreferredSize(new Dimension(0, 40));

        JScrollPane scrollPane = new JScrollPane(tabelHistory);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new BorderLayout(10, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JLabel lblInfo = new JLabel("📝 Total Transaksi: " + dataHistory.size());
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInfo.setForeground(COLOR_PRIMARY);
        infoPanel.add(lblInfo, BorderLayout.WEST);

        JButton btnClearHistory = new JButton("🗑️ Hapus Semua History");
        btnClearHistory.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClearHistory.setBackground(COLOR_DANGER);
        btnClearHistory.setForeground(Color.black);
        btnClearHistory.setFocusPainted(false);
        btnClearHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClearHistory.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus semua riwayat transaksi?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dataHistory.clear();
                simpanHistoryKeFile();
                updateHistoryTable();
                JOptionPane.showMessageDialog(this, "History berhasil dihapus!");
            }
        });
        infoPanel.add(btnClearHistory, BorderLayout.EAST);

        tablePanel.add(infoPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private void updateHistoryTable() {
        modelHistory.setRowCount(0);
        int no = 1;
        for (HistoryEntry entry : dataHistory) {
            modelHistory.addRow(new Object[]{
                    no++,
                    entry.getTanggalWaktu(),
                    entry.getAksi(),
                    entry.getNis(),
                    entry.getNama(),
                    entry.getDetail()
            });
        }
    }

    private void tambahData() {
        if (validasiInput()) {
            try {
                String nis = txtNIS.getText().trim();
                String nama = txtNama.getText().trim();
                String kelas = txtKelas.getText().trim();
                String jurusan = (String) cmbJurusan.getSelectedItem();

                Siswa siswa = new Siswa(nis, nama, kelas, jurusan);
                dataSiswa.add(siswa);

                String detail = String.format("Kelas: %s, Jurusan: %s", kelas, jurusan);
                dataHistory.add(new HistoryEntry("TAMBAH", nis, nama, detail));

                updateTabel();
                updateStatistik();
                simpanDataKeFile();
                simpanHistoryKeFile();
                resetForm();

                JOptionPane.showMessageDialog(this,
                        "✅ Data siswa berhasil ditambahkan!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateData() {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Pilih data siswa dari tabel terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (validasiInput()) {
            try {
                String nis = txtNIS.getText().trim();
                String nama = txtNama.getText().trim();
                String kelas = txtKelas.getText().trim();
                String jurusan = (String) cmbJurusan.getSelectedItem();

                Siswa siswa = dataSiswa.get(selectedRow);
                String oldData = String.format("%s (%s %s)", siswa.getNama(),
                        siswa.getKelas(), siswa.getJurusan());

                siswa.setNis(nis);
                siswa.setNama(nama);
                siswa.setKelas(kelas);
                siswa.setJurusan(jurusan);

                String detail = String.format("Data lama: %s → Baru: %s %s",
                        oldData, kelas, jurusan);
                dataHistory.add(new HistoryEntry("UPDATE", nis, nama, detail));

                updateTabel();
                updateStatistik();
                simpanDataKeFile();
                simpanHistoryKeFile();
                resetForm();

                JOptionPane.showMessageDialog(this,
                        "✅ Data siswa berhasil diupdate!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void hapusData() {
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                    "⚠️ Pilih data siswa dari tabel terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Siswa siswa = dataSiswa.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus data siswa: " + siswa.getNama() + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String detail = String.format("Kelas: %s, Jurusan: %s",
                        siswa.getKelas(), siswa.getJurusan());
                dataHistory.add(new HistoryEntry("HAPUS",
                        siswa.getNis(), siswa.getNama(), detail));

                dataSiswa.remove(selectedRow);

                updateTabel();
                updateStatistik();
                simpanDataKeFile();
                simpanHistoryKeFile();
                resetForm();

                JOptionPane.showMessageDialog(this,
                        "✅ Data siswa berhasil dihapus!",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        txtNIS.setText("");
        txtNama.setText("");
        txtKelas.setText("");
        cmbJurusan.setSelectedIndex(0);
        tabelSiswa.clearSelection();
        selectedRow = -1;
        txtNIS.requestFocus();
    }

    private boolean validasiInput() {
        if (txtNIS.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ NIS belum terisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtNIS.requestFocus();
            return false;
        }
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Nama siswa belum terisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtNama.requestFocus();
            return false;
        }
        if (txtKelas.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠️ Kelas belum terisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            txtKelas.requestFocus();
            return false;
        }
        return true;
    }

    private void updateTabel() {
        modelTabel.setRowCount(0);
        int no = 1;
        for (Siswa siswa : dataSiswa) {
            modelTabel.addRow(new Object[]{
                    no++,
                    siswa.getNis(),
                    siswa.getNama(),
                    siswa.getKelas(),
                    siswa.getJurusan()
            });
        }
    }

    private void tampilkanDataKeForm(int row) {
        Siswa siswa = dataSiswa.get(row);
        txtNIS.setText(siswa.getNis());
        txtNama.setText(siswa.getNama());
        txtKelas.setText(siswa.getKelas());
        cmbJurusan.setSelectedItem(siswa.getJurusan());
    }

    private void simpanDataKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Siswa siswa : dataSiswa) {
                writer.write(siswa.getNis() + "|" +
                        siswa.getNama() + "|" +
                        siswa.getKelas() + "|" +
                        siswa.getJurusan());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error menyimpan data: " + e.getMessage());
        }
    }

    private void muatDataDariFile() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    Siswa siswa = new Siswa(parts[0], parts[1], parts[2], parts[3]);
                    dataSiswa.add(siswa);
                }
            }
        } catch (IOException e) {
            System.err.println("Error memuat data: " + e.getMessage());
        }
    }

    private void simpanHistoryKeFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HISTORY_FILE))) {
            for (HistoryEntry entry : dataHistory) {
                writer.write(entry.getTanggalWaktu() + "|" +
                        entry.getAksi() + "|" +
                        entry.getNis() + "|" +
                        entry.getNama() + "|" +
                        entry.getDetail());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error menyimpan history: " + e.getMessage());
        }
    }

    private void muatHistoryDariFile() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(HISTORY_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    HistoryEntry entry = new HistoryEntry(
                            parts[1], parts[2], parts[3], parts[4]);
                    entry.setTanggalWaktu(parts[0]);
                    dataHistory.add(entry);
                }
            }
        } catch (IOException e) {
            System.err.println("Error memuat history: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Tidak dapat mengatur Look and Feel: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> {
            SistemTambahAnggotaOSIS app = new SistemTambahAnggotaOSIS();
            app.setVisible(true);
            app.updateStatistik();
            app.updateTabel();
        });
    }
}