import java.util.Scanner;

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

    VoterList() {
        head = null;
    }

    void addVoter(String cnic, String name) {
        VoterNode newNode = new VoterNode(cnic, name);
        newNode.next = head;
        head = newNode;
    }

    VoterNode searchVoter(String cnic) {
        VoterNode temp = head;
        while (temp != null) {
            if (temp.cnic.equals(cnic)) {
                return temp;
            }
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
            System.out.println("CNIC: " + curr.cnic + " | Name: " + curr.name + " | Voted: " + curr.hasVoted);
            curr = curr.next;
        }
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
        this.left = null;
        this.right = null;
    }
}

class CandidateBST {

    CandidateNode root;

    CandidateBST() {
        root = null;
    }

    void insertCandidate(String cnic, String name, String party) {
        CandidateNode newNode = new CandidateNode(cnic, name, party);

        if (root == null) {
            root = newNode;
            return;
        }

        CandidateNode current = root;
        CandidateNode parent = null;

        while (current != null) {
            parent = current;
            if (cnic.compareTo(current.cnic) < 0)
                current = current.left;
            else
                current = current.right;
        }

        if (cnic.compareTo(parent.cnic) < 0)
            parent.left = newNode;
        else
            parent.right = newNode;
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

    void inorder() {
        inorderRecursive(root);
    }

    void inorderRecursive(CandidateNode node) {
        if (node == null) return;
        inorderRecursive(node.left);
        System.out.println(node.cnic + " - " + node.name + " (" + node.party + ") | Votes: " + node.votes);
        inorderRecursive(node.right);
    }

    // DISPLAY BY NUMBER
     private int index; // used during traversal
    private CandidateNode selectedCandidate; // used to return node by number

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

    // ===== get node by displayed number =====
    CandidateNode getByNumber(int number) {
        index = 1;
        selectedCandidate = null;
        findByNumberRec(root, number);
        return selectedCandidate;
    }

    private void findByNumberRec(CandidateNode node, int number) {
        if (node == null) return;
        findByNumberRec(node.left, number);
        if (selectedCandidate != null) return; // early exit if found
        if (index == number) {
            selectedCandidate = node;
            return;
        }
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

    void deleteCandidate(String cnic) {
        root = deleteNode(root, cnic);
    }

    CandidateNode findWinner() {
        return findWinnerRec(root, null);
    }

    CandidateNode findWinnerRec(CandidateNode node, CandidateNode best) {
        if (node == null) return best;
        if (best == null || node.votes > best.votes) best = node;

        best = findWinnerRec(node.left, best);
        best = findWinnerRec(node.right, best);
        return best;
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

    void registerVoter(String cnic, String name) {
        voterList.addVoter(cnic, name);
    }

    void registerCandidate(String cnic, String name, String party) {
        candidateTree.insertCandidate(cnic, name, party);
    }

    void verifyAndVote(String voterCnic) {
        VoterNode voter = voterList.searchVoter(voterCnic);

        if (voter == null) {
            System.out.println("Voter not found!");
            return;
        }

        if (voter.hasVoted) {
            System.out.println("You have already voted.");
            return;
        }

        System.out.println("\nSelect a Candidate:");
        candidateTree.displayWithNumbers();

        System.out.print("Enter candidate number: ");
        int num = sc.nextInt();
        sc.nextLine();

        CandidateNode choice = candidateTree.getByNumber(num);

        if (choice == null) {
            System.out.println("Invalid choice!");
            return;
        }

        candidateTree.castVote(choice.cnic);
        voterList.markAsVoted(voterCnic);

        System.out.println("Vote Cast Successfully for " + choice.name);
    }

        // for valid cnic
        boolean isValidCNIC(String cnic) {
        if (cnic.length() != 13) return false;

        for (int i = 0; i < cnic.length(); i++) {
            char ch = cnic.charAt(i);
            if (ch < '0' || ch > '9') return false; // not a digit
        }

        return true;
    }


    void menu() {
        while (true) {
            System.out.println("\n=== E-Voting Menu ===");
            System.out.println("1. Register Voter");
            System.out.println("2. Register Candidate");
            System.out.println("3. Cast Vote");
            System.out.println("4. Show Candidates");
            System.out.println("5. Delete Candidate");
            System.out.println("6. Show Winner");
            System.out.println("7. Display Voters");
            System.out.println("8. Exit");
            System.out.print("Choose: ");

            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Enter CNIC (13 digits, with no hyphen or letter): ");
                    String vcnic = sc.nextLine();
                    if (!isValidCNIC(vcnic)) { //calls the validcnic func
                        System.out.println("Invalid CNIC! Must be exactly 13 digits and numbers only.");
                        break;
                    }
                    System.out.print("Name: ");
                    String vname = sc.nextLine();

                    registerVoter(vcnic, vname);
                    System.out.println("Voter Registered.");
                    break;

                case 2:
                    System.out.print("Candidate CNIC: ");
                    String cnic = sc.nextLine();

                    if (!isValidCNIC(cnic)) {
                        System.out.println("Invalid CNIC! Must be exactly 13 digits and numbers only.");
                        break;
                    }

                    System.out.print("Name: ");
                    String cname = sc.nextLine();
                    System.out.print("Party: ");
                    String party = sc.nextLine();
                    registerCandidate(cnic, cname, party);
                    System.out.println("Candidate Registered.");
                    break;

                case 3:
                    System.out.print("Enter your CNIC: ");
                    String voterInput = sc.nextLine();

                    if (!isValidCNIC(voterInput)) {
                        System.out.println("Invalid CNIC format!");
                        break;
                    }

                    verifyAndVote(voterInput);
                    break;


                case 4:
                    candidateTree.inorder();
                    break;

                case 5:
                    System.out.print("Enter candidate CNIC to delete: ");
                    String dc = sc.nextLine();
                    if (candidateTree.searchCandidate(dc) == null) {
                        System.out.println("Candidate not found.");
                    } else {
                        candidateTree.deleteCandidate(dc);
                        System.out.println("Candidate Removed.");
                    }
                    break;

                case 6:
                    CandidateNode winner = candidateTree.findWinner();
                    if (winner == null)
                        System.out.println("No candidates found.");
                    else
                        System.out.println("Winner: " + winner.name + " (" + winner.party + ") | Votes: " + winner.votes);
                    break;

                case 7:
                    voterList.displayVoters();
                    break;

                case 8:
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}

public class MainCode  {
    public static void main(String[] args) {
        ElectionManager em = new ElectionManager();

        em.registerVoter("4220112345671", "Anees Hussain");
        em.registerVoter("4220123456783", "Ali Khan");
        em.registerVoter("4220134567895", "Sana Iqbal");

        em.registerCandidate("4123412345671", "Imran Khan", "PTI");
        em.registerCandidate("4123412345672", "Nawaz Sharif", "PML-N");
        em.registerCandidate("4123412345673", "Bilawal Bhutto", "PPP");

        em.menu();
    }
}
