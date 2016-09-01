package client.dialog.cases;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import server.dao.AbstractDAO;
import server.dao.LoginDAO;
import server.entities.Usuario;
import sun.misc.BASE64Encoder;
import client.enums.EnumImage;

public class DialogLogin extends JFrame {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginDAO loginDAO = new LoginDAO();

    private JPanel contentPane;
    private JTextField fieldLogin;
    private JPasswordField fieldSenha;

    public AbstractDAO<Usuario> getDAO() {
        return new LoginDAO();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DialogLogin frame = new DialogLogin();
                    frame.setVisible(true);
                    frame.setUndecorated(true);
                    System.exit(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DialogLogin() {
        new ImageIcon(getClass().getResource(EnumImage.PESQUISA.getCaminho()));

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 227);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel labelLogin = new JLabel("Login");
        labelLogin.setForeground(Color.WHITE);
        labelLogin.setBackground(new Color(0, 0, 0));
        labelLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
        labelLogin.setBounds(78, 106, 68, 22);
        contentPane.add(labelLogin);

        JLabel labelSenha = new JLabel("Senha");
        labelSenha.setFont(new Font("Tahoma", Font.BOLD, 14));
        labelSenha.setBounds(78, 145, 68, 22);
        contentPane.add(labelSenha);

        fieldLogin = new JTextField();
        fieldLogin.setBounds(169, 107, 178, 20);
        contentPane.add(fieldLogin);
        fieldLogin.setColumns(10);

        fieldSenha = new JPasswordField();
        fieldSenha.setBounds(169, 148, 178, 20);
        contentPane.add(fieldSenha);
        fieldSenha.setColumns(10);

        setMinimumSize(new Dimension(400, 150));
        setLocationRelativeTo(null);
        setExtendedState((int) Frame.CENTER_ALIGNMENT);
        setUndecorated(true);

        JButton buttonLogar = new JButton("Logar");
        buttonLogar.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent arg0) {
                if (fieldLogin.getText().isEmpty() || fieldSenha.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Os campos precisam ser preenchidos para logar!");
                } else {
                    validarUsuario();
                }

            }
        });
        buttonLogar.setBounds(115, 180, 89, 23);
        contentPane.add(buttonLogar);

        JButton buttonCancelar = new JButton("Cancelar");
        buttonCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        buttonCancelar.setBounds(233, 180, 89, 23);
        contentPane.add(buttonCancelar);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblNewLabel.setBackground(new Color(0, 0, 0));
        lblNewLabel.setIcon(new ImageIcon(DialogLogin.class.getResource(EnumImage.LOGIN.getCaminho())));
        lblNewLabel.setBounds(0, 0, 450, 244);
        contentPane.add(lblNewLabel);
    }

    private void validarUsuario() {
        Usuario usuario = loginDAO.find(fieldLogin.getText(), criptografa(fieldSenha.getText()));
        if (usuario == null) {
//               throw new EJDLogicException(EnumEJDException.USUARIO_SENHA_INVALIDA);
            JOptionPane.showMessageDialog(null, "Usuario ou senha incorreto!");
        } else {
            new FramePrincipal().setVisible(true);

        }
    }

    public String criptografa(String senha) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(senha.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(digest.digest());
        } catch (NoSuchAlgorithmException ns) {
            ns.printStackTrace();
        }
        return senha;
    }
}
