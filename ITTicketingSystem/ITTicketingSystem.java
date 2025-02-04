import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class Ticket {
    private static int counter = 1;
    private int id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private LocalDateTime createdAt;
    private String assignedTo;
    private String department;

    public Ticket(String title, String description, String priority, String department) {
        this.id = counter++;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = "Open";
        this.createdAt = LocalDateTime.now();
        this.assignedTo = "Unassigned";
        this.department = department;
    }

 
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }
    public String getDepartment() { return department; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setStatus(String status) { this.status = status; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public Object[] toTableRow() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Object[]{
                id, title, priority, status, department,
                assignedTo, createdAt.format(formatter)
        };
    }
}

public class ITTicketingSystem extends JFrame {
    private ArrayList<Ticket> tickets = new ArrayList<>();
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityCombo;
    private JComboBox<String> departmentCombo;
    private JComboBox<String> assigneeCombo;
    private JComboBox<String> statusCombo;
    private JButton createButton, updateButton, clearButton;

    private static final String[][] DEPARTMENT_ASSIGNEES = {
            {"ITO", "John Arnold Solilapsi", "Niko Tablante", "Nadeem Lustre", "Ferdinand Magellan"},
            {"PSD", "Hariette Onofre", "Madelyn Santos", "Princess Sara", "Gabby Nah"}
    };

    public ITTicketingSystem() {
        setTitle("SynTix");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));

       
        setLayout(new BorderLayout(10, 10));

       
        createTicketTable();
        JPanel formPanel = createFormPanel();

       
        add(new JScrollPane(ticketTable), BorderLayout.CENTER);
        add(formPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    private void createTicketTable() {
  
        String[] columnNames = {"ID", "Subject", "Priority", "Status",
                "Department", "Assigned To", "Created"};

       
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        
        ticketTable = new JTable(tableModel);

        
        ticketTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.getTableHeader().setReorderingAllowed(false);

      
        ticketTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && ticketTable.getSelectedRow() != -1) {
                displayTicketDetails(ticketTable.getSelectedRow());
            }
        });
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Subject
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Subject:"), gbc);
        titleField = new JTextField(20);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Description:"), gbc);
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descScrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        formPanel.add(descScrollPane, gbc);

        // Department
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Department:"), gbc);
        departmentCombo = new JComboBox<>(new String[]{"ITO", "PSD"});
        gbc.gridx = 1;
        formPanel.add(departmentCombo, gbc);

        // Assignee
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Assign To:"), gbc);
        assigneeCombo = new JComboBox<>();
        departmentCombo.addActionListener(e -> updateAssigneeDropdown());
        updateAssigneeDropdown();
        gbc.gridx = 1;
        formPanel.add(assigneeCombo, gbc);

        // Priority
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Priority:"), gbc);
        priorityCombo = new JComboBox<>(new String[]{"Low", "Medium", "High", "Critical"});
        gbc.gridx = 1;
        formPanel.add(priorityCombo, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Status:"), gbc);
        statusCombo = new JComboBox<>(new String[]{"Open", "In Progress", "Resolved", "Closed"});
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        createButton = new JButton("Create Ticket");
        updateButton = new JButton("Update Ticket");
        clearButton = new JButton("Clear Ticket");


        createButton.setBackground(new Color(255, 152, 0)); 
        createButton.setForeground(Color.ORANGE);


        updateButton.setBackground(new Color(33, 150, 243)); 
        updateButton.setForeground(Color.ORANGE);

        clearButton.setBackground(new Color(244, 67, 54)); 
        clearButton.setForeground(Color.RED);

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        
        createButton.setBackground(new Color(76, 175, 80));
        createButton.setForeground(Color.BLUE);
        updateButton.setBackground(new Color(33, 150, 243));
        updateButton.setForeground(Color.BLUE);
        clearButton.setBackground(new Color(244, 67, 54));
        clearButton.setForeground(Color.BLUE);

        buttonPanel.add(createButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

       
        createButton.addActionListener(e -> createTicket());
        updateButton.addActionListener(e -> updateTicket());
        clearButton.addActionListener(e -> clearTicket());

     
        updateButton.setEnabled(false);
        clearButton.setEnabled(false);

        return formPanel;
    }

    private void updateAssigneeDropdown() {
        String selectedDepartment = (String) departmentCombo.getSelectedItem();
        for (String[] dept : DEPARTMENT_ASSIGNEES) {
            if (dept[0].equals(selectedDepartment)) {
                String[] assignees = new String[dept.length - 1];
                System.arraycopy(dept, 1, assignees, 0, dept.length - 1);
                assigneeCombo.setModel(new DefaultComboBoxModel<>(assignees));
                break;
            }
        }
    }

    private void createTicket() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();

        if (title.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in both subject and description.",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Ticket ticket = new Ticket(
                title,
                description,
                (String) priorityCombo.getSelectedItem(),
                (String) departmentCombo.getSelectedItem()
        );

        tickets.add(ticket);
        tableModel.addRow(ticket.toTableRow());
        clearForm();
    }

    private void updateTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow >= 0) {
            Ticket ticket = tickets.get(selectedRow);
            ticket.setStatus((String) statusCombo.getSelectedItem());
            ticket.setAssignedTo((String) assigneeCombo.getSelectedItem());

            tableModel.setValueAt(ticket.getStatus(), selectedRow, 3);
            tableModel.setValueAt(ticket.getAssignedTo(), selectedRow, 5);

            clearForm();
            updateButton.setEnabled(false);
            clearButton.setEnabled(false);
            createButton.setEnabled(true);
        }
    }

    private void clearTicket() {
        int selectedRow = ticketTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to remove this ticket?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tickets.remove(selectedRow);
                tableModel.removeRow(selectedRow);
                clearForm();
                updateButton.setEnabled(false);
                clearButton.setEnabled(false);
                createButton.setEnabled(true);
            }
        }
    }

    private void displayTicketDetails(int selectedRow) {
        Ticket ticket = tickets.get(selectedRow);

        titleField.setText(ticket.getTitle());
        descriptionArea.setText(ticket.getDescription());
        departmentCombo.setSelectedItem(ticket.getDepartment());
        updateAssigneeDropdown();
        priorityCombo.setSelectedItem(ticket.getPriority());
        statusCombo.setSelectedItem(ticket.getStatus());
        assigneeCombo.setSelectedItem(ticket.getAssignedTo());

        updateButton.setEnabled(true);
        clearButton.setEnabled(true);
        createButton.setEnabled(false);
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        departmentCombo.setSelectedIndex(0);
        updateAssigneeDropdown();
        priorityCombo.setSelectedIndex(0);
        statusCombo.setSelectedIndex(0);
        assigneeCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
               
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            ITTicketingSystem system = new ITTicketingSystem();
            system.setVisible(true);
        });
    }
}


