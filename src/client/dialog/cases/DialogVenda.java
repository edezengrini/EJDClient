package client.dialog.cases;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import server.dao.AbstractDAO;
import server.dao.ProdutoDAO;
import server.dao.VendaDAO;
import server.ejb.ProdutoEJB;
import server.entities.Produto;
import server.entities.Venda;
import server.entities.abstracts.EntityState;
import server.persistence.Persistence;
import arquitetura.client.components.DecimalFormattedField;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import arquitetura.common.util.DateUtil;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.dialog.DialogLocalizar;
import client.dialog.util.UtilClient;
import client.enums.EnumImage;
import client.table.TableModelEntidade.ITableModelEntidade;
import common.util.UtilCommon;

public class DialogVenda extends DialogCrud<Venda> {
    private static final long serialVersionUID = 1L;

    @Inject
    private ProdutoEJB produtoEJB = new ProdutoEJB();
    private Produto produto = new Produto();

    private final JLabel lbIdentificador = new JLabel("Identificador:");
    private final JLabel lbQuantidade = new JLabel("Quantidade:");
    private final JLabel lbData = new JLabel("Data da Venda:");
    private final JTextField fieldIdentificador = new JTextField();
    private final JTextField fieldQuantidade = new DecimalFormattedField(DecimalFormattedField.NUMERO4);
    private final JTextField fieldData = new JTextField();
    private final JLabel lblProduto = new JLabel("Produto:");
    private final JTextField fieldProduto = new JTextField();
    private final JPanel panel = new JPanel();
    private final JButton btnProduto = new JButton("");

    public DialogVenda(Window window) {
        super(window);
        panel.setLayout(new BorderLayout(0, 0));

        initComponents();
    }

    private void initComponents() {
        panelPrincipal.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

        panelPrincipal.add(this.lbIdentificador, "cell 0 0,alignx trailing");

        panelPrincipal.add(this.fieldIdentificador, "cell 1 0,growx");

        panelPrincipal.add(this.lblProduto, "cell 0 1,alignx trailing");

        panelPrincipal.add(panel, "cell 1 1,grow");

        panelPrincipal.add(this.lbQuantidade, "cell 0 2,alignx trailing");

        panelPrincipal.add(this.fieldQuantidade, "cell 1 2,growx");

        panelPrincipal.add(this.lbData, "cell 0 3,alignx trailing");

        panelPrincipal.add(this.fieldData, "cell 1 3,growx");

        panel.add(fieldProduto);
        fieldProduto.setColumns(10);
        btnProduto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarProduto();
            }
        });
        btnProduto.setIcon(new ImageIcon(DialogVenda.class.getResource(EnumImage.LOCALIZAR.getCaminho())));

        panel.add(btnProduto, BorderLayout.EAST);

    }

    @Override
    public JComponent getFirstFocusable() {
        return btnProduto;
    }

    public static void main(String[] args) {

        try {
            new DialogVenda(null).setVisible(true);
        } finally {
            Persistence.closeAll();
            System.exit(0);
        }

    }

    @Override
    public AbstractDAO<Venda> getDAO() {
        return new VendaDAO();
    }

    @Override
    public String getTitulo() {
        return "Venda";
    }

    @Override
    public void preecherCampos(Venda entidade) {
        fieldIdentificador.setText(entidade != null && entidade.getId() != null ? entidade.getId().toString() : null);
        fieldQuantidade.setText(entidade != null ? UtilCommon.toStringNotNull(entidade.getQuantidade()) : null);
        fieldData.setText(entidade != null ? UtilCommon.formatDate(entidade.getDataVenda()) : null);
        if (entidade != null && entidade.getProduto() != null) {
            fieldProduto.setText(UtilCommon.toStringNotNull(entidade.getProduto()));
            produto = entidade.getProduto();

        }
    }

    @Override
    public void preecherEntidade(Venda entidade) throws EJDLogicException {

        entidade.setProduto(produto);

        BigDecimal quantidade = new BigDecimal(UtilCommon.convertInteiro(fieldQuantidade.getText()));
        entidade.setQuantidade(quantidade);
        DateUtil dataUtil = new DateUtil();

        entidade.setDataVenda(fieldData.getText().isEmpty() || fieldData.getText().equals("  /  /    ") ? new Date() : dataUtil.parseDateDDMMYYYY(fieldData.getText()));

        validarCamposObrigatorios(entidade);
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
        fieldQuantidade.setEnabled(habilita);
        fieldData.setEnabled(false);
        btnProduto.setEnabled(habilita);
        fieldProduto.setEnabled(false);

    }

    @Override
    public ITableModelEntidade<Venda> getTableModelEntidade() {

        return new ITableModelEntidade<Venda>() {
            @Override
            public Object getCampoEntidade(Venda entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getId();
                case 1:
                    return entidade.getProduto();
                case 2:
                    return entidade.getQuantidade();
                }
                return null;
            }

            @Override
            public List<Venda> getEntidades() {
                return dao.localizarTodos();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Identificador", "Produto", "Quantidade" };
            }

        };
    }

    protected void consultarProduto() {

        try {
            setCursor(UtilClient.CURSOR_WAIT);
            ProdutoDAO produtoDao = new ProdutoDAO();

            DialogCrud<Produto> dialogProduto = new DialogProduto(getOwner());

            DialogLocalizar<Produto> formLocalizar = new DialogLocalizar<Produto>(dialogProduto, produtoDao.localizarTodos());
            formLocalizar.setVisible(true);

            Produto entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

            if (entidadeSelecionada != null) {
                fieldProduto.setText(entidadeSelecionada.getDescricao());
                produto = produtoEJB.find(entidadeSelecionada.getIdProduto());
            }

            if (produto == null) {
                throw new EJDLogicException(EnumEJDException.ENTITY_FIND, "Produto");
            }
        } catch (Exception e) {
            reportException(e);
        } finally {
            setCursor(UtilClient.CURSOR_DEFAULT);
        }
    }

    @Override
    public void validarCamposObrigatorios(Venda entidade) throws EJDLogicException {
        ValidateEntity validateEntity = new ValidateEntity();

        validateEntity.validateNotNull(entidade.getProduto(), "Produto");
        validateEntity.validateNotNull(entidade.getQuantidade(), "Quantidade");

        validateEntity.doValidate();

    }

}
