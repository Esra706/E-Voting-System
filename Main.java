import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.util.Scanner;

// ========================= ORIGINAL CLASSES =========================
class VoterNode {
    String cnic;
    String name;
    boolean hasVoted;
    VoterNode next;
    VoterNode(String cnic, String name) {
        this.cnic = cnic;
        this.name = name;
        this.hasVoted = false;
        this.next = null;
    }
}
class VoterList {
    VoterNode head;
    VoterList() { head = null; }
    void addVoter(String cnic, String name) {
        VoterNode newNode = new VoterNode(cnic, name);
        newNode.next = head;
        head = newNode;
    }
    VoterNode searchVoter(String cnic) {
        VoterNode temp = head;
        while (temp != null) {
            if (temp.cnic.equals(cnic)) { return temp; }
            temp = temp.next;
        }
        return null;
    }
    public boolean verifyVoter(String cnic) {
        VoterNode verify = searchVoter(cnic);
        return verify != null && !verify.hasVoted;
    }
    public void markAsVoted(String cnic) {
        VoterNode verify = searchVoter(cnic);
        if (verify != null) verify.hasVoted = true;
    }
    public void displayVoters() {
        VoterNode curr = head;
        System.out.println("Voter List:");
        while (curr != null) {
            System.out.println("CNIC: " + curr.cnic + " \n Name: " + curr.name + " \n Voted: " + curr.hasVoted);
            curr = curr.next;
        }
    }
    public List<String[]> toList() {
        List<String[]> list = new ArrayList<>();
        VoterNode curr = head;
        while (curr != null) {
            list.add(new String[]{curr.cnic, curr.name, curr.hasVoted ? "Yes" : "No"});
            curr = curr.next;
        }
        return list;
    }
}
class CandidateNode {
    String cnic;
    String name;
    String party;
    int votes;
    CandidateNode left, right;
    CandidateNode(String cnic, String name, String party) {
        this.cnic = cnic;
        this.name = name;
        this.party = party;
        this.votes = 0;
        this.left = null; this.right = null;
    }
}
class CandidateBST {
    CandidateNode root;
    CandidateBST() { root = null; }
    void insertCandidate(String cnic, String name, String party) {
        CandidateNode newNode = new CandidateNode(cnic, name, party);
        if (root == null) { root = newNode; return; }
        CandidateNode current = root, parent = null;
        while (current != null) {
            parent = current;
            if (cnic.compareTo(current.cnic) < 0) current = current.left;
            else current = current.right;
        }
        if (cnic.compareTo(parent.cnic) < 0) parent.left = newNode;
        else parent.right = newNode;
    }
    CandidateNode searchCandidate(String cnic) {
        CandidateNode current = root;
        while (current != null) {
            int cmp = cnic.compareTo(current.cnic);
            if (cmp == 0) return current;
            current = (cmp < 0) ? current.left : current.right;
        }
        return null;
    }
    boolean castVote(String candidateCnic) {
        CandidateNode cand = searchCandidate(candidateCnic);
        if (cand == null) return false;
        cand.votes++;
        return true;
    }
    void inorder() { inorderRecursive(root); }
    void inorderRecursive(CandidateNode node) {
        if (node == null) return;
        inorderRecursive(node.left);
        System.out.println(node.cnic + " - " + node.name + " (" + node.party + ") \n Votes: " + node.votes);
        inorderRecursive(node.right);
    }
    private int index;
    private CandidateNode selectedCandidate;
    void displayWithNumbers() {
        index = 1;
        displayWithNumbersRec(root);
    }
    private void displayWithNumbersRec(CandidateNode node) {
        if (node == null) return;
        displayWithNumbersRec(node.left);
        System.out.println(index + ". " + node.name + " (" + node.party + ") [" + node.cnic + "]");
        index++;
        displayWithNumbersRec(node.right);
    }
    CandidateNode getByNumber(int number) {
        index = 1;
        selectedCandidate = null;
        findByNumberRec(root, number);
        return selectedCandidate;
    }
    private void findByNumberRec(CandidateNode node, int number) {
        if (node == null) return;
        findByNumberRec(node.left, number);
        if (selectedCandidate != null) return;
        if (index == number) { selectedCandidate = node; return; }
        index++;
        findByNumberRec(node.right, number);
    }
    CandidateNode deleteNode(CandidateNode root, String cnic) {
        if (root == null) return null;
        if (cnic.compareTo(root.cnic) < 0)
            root.left = deleteNode(root.left, cnic);
        else if (cnic.compareTo(root.cnic) > 0)
            root.right = deleteNode(root.right, cnic);
        else {
            if (root.left == null) return root.right;
            if (root.right == null) return root.left;
            CandidateNode successor = findMin(root.right);
            root.cnic = successor.cnic;
            root.name = successor.name;
            root.party = successor.party;
            root.votes = successor.votes;
            root.right = deleteNode(root.right, successor.cnic);
        }
        return root;
    }
    CandidateNode findMin(CandidateNode node) {
        while (node.left != null) node = node.left;
        return node;
    }
    void deleteCandidate(String cnic) { root = deleteNode(root, cnic); }
    CandidateNode findWinner() { return findWinnerRec(root, null); }
    CandidateNode findWinnerRec(CandidateNode node, CandidateNode best) {
        if (node == null) return best;
        if (best == null || node.votes > best.votes) best = node;
        best = findWinnerRec(node.left, best);
        best = findWinnerRec(node.right, best);
        return best;
    }
    public List<String[]> toList() {
        List<String[]> list = new ArrayList<>();
        fillList(root, list);
        return list;
    }
    private void fillList(CandidateNode node, List<String[]> list) {
        if (node == null) return;
        fillList(node.left, list);
        list.add(new String[]{node.cnic, node.name, node.party, String.valueOf(node.votes)});
        fillList(node.right, list);
    }
}
class ElectionManager {
    VoterList voterList;
    CandidateBST candidateTree;
    Scanner sc;
    ElectionManager() {
        voterList = new VoterList();
        candidateTree = new CandidateBST();
        sc = new Scanner(System.in);
    }
    void registerVoter(String cnic, String name) { voterList.addVoter(cnic, name); }
    void registerCandidate(String cnic, String name, String party) { candidateTree.insertCandidate(cnic, name, party); }
    void verifyAndVote(String voterCnic) {
        VoterNode voter = voterList.searchVoter(voterCnic);
        if (voter == null) { System.out.println("Voter not found!"); return; }
        if (voter.hasVoted) { System.out.println("You have already voted."); return; }
        System.out.println("\nSelect a Candidate:");
        candidateTree.displayWithNumbers();
        System.out.print("Enter candidate number: ");
        int num = sc.nextInt(); sc.nextLine();
        CandidateNode choice = candidateTree.getByNumber(num);
        if (choice == null) { System.out.println("Invalid choice!"); return; }
        candidateTree.castVote(choice.cnic);
        voterList.markAsVoted(voterCnic);
        System.out.println("Vote Cast Successfully for " + choice.name);
    }
    boolean isValidCNIC(String cnic) {
        if (cnic.length() != 13) return false;
        for (char ch : cnic.toCharArray()) if (!Character.isDigit(ch)) return false;
        return true;
    }
}

// ========================= UPDATED SWING GUI =========================
class EVotingGUI extends JFrame {
    private final ElectionManager em;
    private JTextField voterCnicField, voterNameField;
    private JTextField candCnicField, candNameField, candPartyField;
    private JTextField castVoterCnicField, deleteCandCnicField;
    private JComboBox<String> candidateCombo;
    private JTable candidatesTable, votersTable;
    private DefaultTableModel candidatesModel, votersModel;
    private JLabel winnerLabel;

    EVotingGUI(ElectionManager em) {
        this.em = em;
        setTitle("E-Voting System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);

        // Nimbus L&F + custom theme
        try { for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ignored) {}
        UIManager.put("control", new Color(245, 248, 255));
        UIManager.put("nimbusBase", new Color(60,100,160));
        UIManager.put("nimbusBlueGrey", new Color(90,120,160));
        UIManager.put("nimbusSelectionBackground", new Color(70,120,200));

        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);
        add(buildTabs(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        refreshTables();
        refreshCandidateCombo();
    }

    private JComponent buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(new EmptyBorder(16,16,8,16));
        header.setBackground(new Color(40,75,135));

        JLabel title = new JLabel("E-Voting Management", UIManager.getIcon("OptionPane.informationIcon"), JLabel.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        JLabel sub = new JLabel("Register • Vote • View • Manage");
        sub.setFont(sub.getFont().deriveFont(Font.PLAIN, 14f));
        sub.setForeground(new Color(220,220,220));
        header.add(sub, BorderLayout.SOUTH);
        return header;
    }

    private JComponent buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(new Color(200,220,255));
        tabs.setForeground(Color.BLACK);
        tabs.setBorder(BorderFactory.createLineBorder(new Color(100,120,150), 2));

        tabs.addTab("Register Voter", buildRegisterVoterPanel());
        tabs.addTab("Register Candidate", buildRegisterCandidatePanel());
        tabs.addTab("Cast Vote", buildCastVotePanel());
        tabs.addTab("Candidates", buildCandidatesPanel());
        tabs.addTab("Delete Candidate", buildDeleteCandidatePanel());
        tabs.addTab("Winner", buildWinnerPanel());
        tabs.addTab("Voters", buildVotersPanel());
        return tabs;
    }

    private JComponent buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBorder(new EmptyBorder(8,16,16,16));
        JButton refreshBtn = coloredButton("Refresh Data");
        refreshBtn.addActionListener(e -> { refreshTables(); refreshCandidateCombo(); updateWinnerLabel(); });
        footer.add(refreshBtn);
        footer.setBackground(new Color(225,240,255));
        return footer;
    }

    private JPanel buildRegisterVoterPanel() {
        JPanel p = cardPanel();
        voterCnicField = new JTextField(18);
        voterNameField = new JTextField(18);
        p.add(labeledField("CNIC (13 digits):", voterCnicField));
        p.add(labeledField("Name:", voterNameField));
        JButton addBtn = coloredButton("Register Voter");
        addBtn.addActionListener(e -> onRegisterVoter());
        p.add(addBtn);
        return wrap(p);
    }

    private JPanel buildRegisterCandidatePanel() {
        JPanel p = cardPanel();
        candCnicField = new JTextField(18);
        candNameField = new JTextField(18);
        candPartyField = new JTextField(18);
        p.add(labeledField("Candidate CNIC:", candCnicField));
        p.add(labeledField("Name:", candNameField));
        p.add(labeledField("Party:", candPartyField));
        JButton addBtn = coloredButton("Register Candidate");
        addBtn.addActionListener(e -> onRegisterCandidate());
        p.add(addBtn);
        return wrap(p);
    }

    private JPanel buildCastVotePanel() {
        JPanel p = cardPanel();
        castVoterCnicField = new JTextField(18);
        candidateCombo = new JComboBox<>();
        p.add(labeledField("Your CNIC:", castVoterCnicField));
        p.add(labeledField("Select Candidate:", candidateCombo));
        JButton voteBtn = coloredButton("Cast Vote");
        voteBtn.addActionListener(e -> onCastVote());
        p.add(voteBtn);
        return wrap(p);
    }

    private JPanel buildDeleteCandidatePanel() {
        JPanel p = cardPanel();
        deleteCandCnicField = new JTextField(18);
        p.add(labeledField("Candidate CNIC to delete:", deleteCandCnicField));
        JButton delBtn = coloredButton("Delete Candidate");
        delBtn.addActionListener(e -> onDeleteCandidate());
        p.add(delBtn);
        return wrap(p);
    }

    private JPanel buildWinnerPanel() {
        JPanel p = cardPanel();
        winnerLabel = new JLabel("Winner: —");
        winnerLabel.setFont(winnerLabel.getFont().deriveFont(Font.BOLD, 16f));
        p.add(winnerLabel);
        JButton calcBtn = coloredButton("Calculate Winner");
        calcBtn.addActionListener(e -> updateWinnerLabel());
        p.add(calcBtn);
        return wrap(p);
    }

    private JPanel buildCandidatesPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(12,12,12,12));

        candidatesModel = new DefaultTableModel(new String[]{"CNIC","Name","Party","Votes"},0) {
            @Override public boolean isCellEditable(int row,int column){return false;}
        };
        candidatesTable = new JTable(candidatesModel);
        candidatesTable.setRowHeight(22);
        setTableColors(candidatesTable);
        p.add(new JScrollPane(candidatesTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBackground(new Color(225,240,255));
        JButton refresh = coloredButton("Refresh");
        refresh.addActionListener(e -> refreshTables());
        actions.add(refresh);
        p.add(actions, BorderLayout.SOUTH);
        return p;
    }

    private JPanel buildVotersPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(12,12,12,12));

        votersModel = new DefaultTableModel(new String[]{"CNIC","Name","Has Voted"},0) {
            @Override public boolean isCellEditable(int row,int column){return false;}
        };
        votersTable = new JTable(votersModel);
        votersTable.setRowHeight(22);
        setTableColors(votersTable);
        p.add(new JScrollPane(votersTable), BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBackground(new Color(225,240,255));
        JButton refresh = coloredButton("Refresh");
        refresh.addActionListener(e -> refreshTables());
        actions.add(refresh);
        p.add(actions, BorderLayout.SOUTH);
        return p;
    }

    // ------------------------ Helpers ------------------------
    private void onRegisterVoter() {
        String cnic = voterCnicField.getText().trim();
        String name = voterNameField.getText().trim();
        if (!em.isValidCNIC(cnic)) { toast("Invalid CNIC!"); return; }
        if (name.isEmpty()) { toast("Enter voter name."); return; }
        if (em.voterList.searchVoter(cnic)!=null){toast("Voter exists."); return;}
        em.registerVoter(cnic,name);
        toast("Voter registered.");
        voterCnicField.setText(""); voterNameField.setText("");
        refreshTables();
    }

    private void onRegisterCandidate() {
        String cnic = candCnicField.getText().trim();
        String name = candNameField.getText().trim();
        String party = candPartyField.getText().trim();
        if(!em.isValidCNIC(cnic)){toast("Invalid CNIC"); return;}
        if(name.isEmpty()||party.isEmpty()){toast("Enter name and party"); return;}
        if(em.candidateTree.searchCandidate(cnic)!=null){toast("Candidate exists"); return;}
        em.registerCandidate(cnic,name,party);
        toast("Candidate registered.");
        candCnicField.setText(""); candNameField.setText(""); candPartyField.setText("");
        refreshTables(); refreshCandidateCombo();
    }

    private void onCastVote() {
        String voterCnic = castVoterCnicField.getText().trim();
        if(!em.isValidCNIC(voterCnic)){toast("Invalid CNIC"); return;}
        VoterNode voter = em.voterList.searchVoter(voterCnic);
        if(voter==null){toast("Voter not found"); return;}
        if(voter.hasVoted){toast("Already voted"); return;}
        String selected = (String)candidateCombo.getSelectedItem();
        if(selected==null){toast("Select candidate"); return;}
        String candidateCnic = selected.substring(selected.lastIndexOf('[')+1,selected.lastIndexOf(']'));
        if(!em.candidateTree.castVote(candidateCnic)){toast("Candidate not found"); return;}
        em.voterList.markAsVoted(voterCnic);
        toast("Vote cast successfully!");
        refreshTables(); updateWinnerLabel();
    }

    private void onDeleteCandidate() {
        String cnic = deleteCandCnicField.getText().trim();
        if(!em.isValidCNIC(cnic)){toast("Invalid CNIC"); return;}
        if(em.candidateTree.searchCandidate(cnic)==null){toast("Candidate not found"); return;}
        em.candidateTree.deleteCandidate(cnic);
        toast("Candidate removed.");
        deleteCandCnicField.setText("");
        refreshTables(); refreshCandidateCombo(); updateWinnerLabel();
    }

    private void updateWinnerLabel(){
        CandidateNode w = em.candidateTree.findWinner();
        winnerLabel.setText(w==null?"Winner: —":"Winner: "+w.name+" ("+w.party+") — Votes: "+w.votes);
    }

    private void refreshTables(){
        candidatesModel.setRowCount(0);
        for(String[] row: em.candidateTree.toList()) candidatesModel.addRow(row);
        votersModel.setRowCount(0);
        for(String[] row: em.voterList.toList()) votersModel.addRow(row);
    }

    private void refreshCandidateCombo(){
        candidateCombo.removeAllItems();
        for(String[] row: em.candidateTree.toList()) candidateCombo.addItem(row[1]+" ("+row[2]+") ["+row[0]+"]");
    }

    private JPanel cardPanel(){
        JPanel p = new JPanel(new GridLayout(0,1,10,10));
        p.setBorder(new EmptyBorder(12,12,12,12));
        p.setBackground(new Color(230,240,255));
        return p;
    }

    private JPanel labeledField(String label, JComponent field){
        JPanel row = new JPanel(new BorderLayout(10,6));
        row.setBackground(new Color(230,240,255));
        JLabel l = new JLabel(label);
        row.add(l,BorderLayout.WEST);
        row.add(field,BorderLayout.CENTER);
        return row;
    }

    private JPanel wrap(JComponent inner){
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(new Color(230,240,255));
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(new EmptyBorder(20,24,20,24));
        card.add(inner,BorderLayout.NORTH);
        outer.add(card,new GridBagConstraints());
        return outer;
    }

    private void toast(String msg){
        JOptionPane.showMessageDialog(this,msg,"Info",JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton coloredButton(String text){
        JButton b = new JButton(text);
        b.setBackground(new Color(70,130,180));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        return b;
    }

    private void setTableColors(JTable table){
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable t,Object value,boolean isSelected,boolean hasFocus,int row,int column){
                Component c = super.getTableCellRendererComponent(t,value,isSelected,hasFocus,row,column);
                if(row%2==0) c.setBackground(new Color(220,235,255));
                else c.setBackground(new Color(245,250,255));
                if(isSelected) c.setBackground(new Color(100,160,210));
                return c;
            }
        });
    }
}

// ========================= MAIN =========================
public class Main {
    public static void main(String[] args) {
        ElectionManager em = new ElectionManager();
        em.registerVoter("4220112345671","Anees Hussain");
        em.registerVoter("4220123456783","Ali Khan");
        em.registerVoter("4220134567895","Sana Iqbal");
        em.registerCandidate("4123412345671","Imran Khan","PTI");
        em.registerCandidate("4123412345672","Nawaz Sharif","PML-N");
        em.registerCandidate("4123412345673","Bilawal Bhutto","PPP");

        SwingUtilities.invokeLater(() -> {
            EVotingGUI gui = new EVotingGUI(em);
            gui.setVisible(true);
        });
    }
}
