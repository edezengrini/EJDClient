package client.dialog.cases;

import java.awt.Window;

import javax.swing.JComponent;

import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.table.TableModelEntidade.ITableModelEntidade;
import net.miginfocom.swing.MigLayout;
import server.dao.AbstractDAO;
import server.dao.UsuarioDAO;
import server.entities.Usuario;
import server.entities.abstracts.EntityState;
import sun.misc.BASE64Encoder;

import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.awt.event.ActionEvent;

public class DialogUsuario extends DialogCrud<Usuario> {

    public DialogUsuario(Window window) {
        super(window);
        initComponents();

    }

    private void initComponents() {
        panelPrincipal.setLayout(new MigLayout("", "[][grow]", "[][][][][][]"));

        labelIdentificador = new JLabel("Identificador");
        panelPrincipal.add(labelIdentificador, "cell 0 0,alignx trailing");

        fieldIdentificador = new JTextField();
        panelPrincipal.add(fieldIdentificador, "cell 1 0,growx");
        fieldIdentificador.setColumns(10);

        JLabel labelNome = new JLabel(" Nome");
        panelPrincipal.add(labelNome, "cell 0 1,alignx trailing");

        fieldNome = new JTextField();
        panelPrincipal.add(fieldNome, "cell 1 1,growx");
        fieldNome.setColumns(10);

        JLabel LabelEmail = new JLabel("Email");
        panelPrincipal.add(LabelEmail, "cell 0 2,alignx trailing,aligny baseline");

        fieldEmail = new JTextField();
        panelPrincipal.add(fieldEmail, "cell 1 2,growx");
        fieldEmail.setColumns(10);

        JLabel labelLogin = new JLabel("Login");
        panelPrincipal.add(labelLogin, "cell 0 3,alignx trailing");

        fieldLogin = new JTextField();
        panelPrincipal.add(fieldLogin, "cell 1 3,growx");
        fieldLogin.setColumns(10);

        JLabel labelSenha = new JLabel("Senha");
        labelSenha.setHorizontalAlignment(SwingConstants.RIGHT);
        panelPrincipal.add(labelSenha, "cell 0 4,alignx trailing,aligny baseline");

        fieldSenha = new JPasswordField();
        panelPrincipal.add(fieldSenha, "flowx,cell 1 4,growx");
        fieldSenha.setColumns(10);

        JLabel labelPermissao = new JLabel("Permiss\u00E3o");
        panelPrincipal.add(labelPermissao, "cell 1 4");

        comboBoxPermissao = new JComboBox(new Object[] { " ", "1", "2", "3", "4", "5" });
        comboBoxPermissao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //   while(comboBoxPermissao.getModel().getSize() > 0)comboBoxPermissao.removeItemAt(0);

            }
        });
        panelPrincipal.add(comboBoxPermissao, "cell 1 4,growx");

        checkBoxAtivo = new JCheckBox("Ativo");
        checkBoxAtivo.setHorizontalAlignment(SwingConstants.RIGHT);
        panelPrincipal.add(checkBoxAtivo, "cell 1 4,growx");

    }

    private static final long serialVersionUID = 1L;
    private JTextField fieldNome;
    private JTextField fieldEmail;
    private JTextField fieldLogin;
    private JPasswordField fieldSenha;
    private JCheckBox checkBoxAtivo;
    private JComboBox comboBoxPermissao;
    private JLabel labelIdentificador;
    private JTextField fieldIdentificador;

    @Override
    public AbstractDAO<Usuario> getDAO() {
        return new UsuarioDAO();
    }

    @Override
    public void preecherEntidade(Usuario entidade) throws EJDLogicException {
        entidade.setNome(fieldNome.getText());
        entidade.setEmail(fieldEmail.getText());
        entidade.setLogin(fieldLogin.getText());
        entidade.setSenha(criptografa(fieldSenha.getText()));
        entidade.setStatus(checkBoxAtivo.isSelected());
        entidade.setDataCadastro(new Date());
        if (comboBoxPermissao.getSelectedIndex() == 1) {
            entidade.isAdministrador();
        } else {
            entidade.setPermissao(comboBoxPermissao.getSelectedIndex() == 0 ? false : true);
        }
    }

    @Override
    public void preecherCampos(Usuario entidade) {
        fieldIdentificador.setText(entidade != null && entidade.getIdUsuario() != null ? entidade.getIdUsuario().toString() : null);
        fieldNome.setText(entidade != null ? entidade.getNome() : null);
        fieldEmail.setText(entidade != null ? entidade.getEmail() : null);
        fieldLogin.setText(entidade != null ? entidade.getLogin() : null);
        fieldSenha.setText(entidade != null ? entidade.getSenha() : null);
        comboBoxPermissao.setSelectedIndex((entidade != null && entidade.getPermissao() == 1) ? entidade.getPermissao() : 0);

    }

    @Override
    public void habilitarCampos(EntityState state) {
        boolean habilita = false;
        switch (state) {
        case DELETED:
        case UNMODIFIED:
            habilita = false;
            break;
        case NEW:
        case MODIFIED:
            habilita = true;
            break;
        }
        fieldIdentificador.setEnabled(false);
        fieldNome.setEnabled(habilita);
        fieldEmail.setEnabled(habilita);
        fieldLogin.setEnabled(habilita);
        fieldSenha.setEnabled(habilita);
    }

    @Override
    public ITableModelEntidade<Usuario> getTableModelEntidade() {

        return new ITableModelEntidade<Usuario>() {

            @Override
            public Object getCampoEntidade(Usuario entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getIdUsuario();
                case 1:
                    return entidade.getNome();
                case 2:
                    return entidade.getEmail();
                case 3:
                    return entidade.getLogin();
                case 4:
                    return entidade.getSenha();
                case 5:
                    return entidade.isAtivo();
                case 6:
                    return entidade.getPermissao();
                }
                return null;
            }

            @Override
            public List<Usuario> getEntidades() {
                return dao.localizarTodos();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Identificador", "Nome", "Email", "Login", "Senha", "Ativo", "Permissao" };
            }

        };

    }

    @Override
    public void validarCamposObrigatorios(Usuario entidade) throws EJDLogicException {
        ValidateEntity validateEntidy = new ValidateEntity();
        validateEntidy.validateNotNull(entidade.getNome(), "nome");
        validateEntidy.validateNotNull(entidade.getEmail(), "email");
        validateEntidy.validateNotNull(entidade.getLogin(), "login");
        validateEntidy.validateNotNull(entidade.getSenha(), "senha");
        validateEntidy.validateNotNull(entidade.isAtivo(), "ativo");
        validateEntidy.validateNotNull(entidade.getPermissao(), "permissao");

        validateEntidy.doValidate();

    }

    @Override
    public JComponent getFirstFocusable() {
        return fieldNome;
    }

    @Override
    public String getTitulo() {
        return "Cadastrar Usuario";
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
