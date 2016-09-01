package client.dialog.cases;

import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;

import server.dao.AbstractDAO;
import server.dao.GrupoDAO;
import server.ejb.GrupoEJB;
import server.entities.Grupo;
import server.entities.abstracts.EntityState;
import server.persistence.Persistence;
import arquitetura.client.components.layout.VerticalFlowLayout;
import arquitetura.client.components.panel.EJDPanelCodigoDescricao;
import arquitetura.client.components.textfield.EJDTextField;
import arquitetura.client.components.textfield.EnumFiltroTextField;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.util.ValidateEntity;
import client.dialog.DialogCrud;
import client.table.TableModelEntidade.ITableModelEntidade;

public class DialogGrupo extends DialogCrud<Grupo> {
    private static final long serialVersionUID = 1L;

//    private final EJDLabel lblIdentificador = new EJDLabel("Identificador:");
//    private final EJDLabel lblDescricao = new EJDLabel("Descri\u00E7\u00E3o:");
    private final EJDTextField fieldIdentificador = new EJDTextField(EnumFiltroTextField.NUMEROS);
    private final EJDTextField fieldDescricao = new EJDTextField(EnumFiltroTextField.LETRAS_NUMEROS);

    private EJDPanelCodigoDescricao panelGrupo = new EJDPanelCodigoDescricao();

    private GrupoEJB grupoEJB = new GrupoEJB();

    public DialogGrupo(Window window) {
        super(window);

        initComponents();
    }

    private void initComponents() {
        panelPrincipal.setLayout(new VerticalFlowLayout());
        panelPrincipal.add(panelGrupo);
        panelGrupo.getPanelCodigo().add(fieldIdentificador);
        panelGrupo.getPanelDescricao().add(fieldDescricao);
//        panelPrincipal.setLayout(new MigLayout("", "[][grow]", "[][][][]"));
//
//        panelPrincipal.add(this.lblIdentificador, "cell 0 0,alignx trailing");
//
//        panelPrincipal.add(this.fieldIdentificador, "cell 1 0,growx");
//
//        panelPrincipal.add(this.lblDescricao, "cell 0 1,alignx trailing");
//
//        panelPrincipal.add(this.fieldDescricao, "cell 1 1,growx");
    }

    @Override
    public JComponent getFirstFocusable() {
        return fieldDescricao;
    }

    public static void main(String[] args) throws EJDLogicException {

        try {
            new DialogGrupo(null).setVisible(true);

        } finally {
            Persistence.closeAll();
        }
    }

    @Override
    public AbstractDAO<Grupo> getDAO() {
        return new GrupoDAO();
    }

    @Override
    public String getTitulo() {
        return "Grupo";
    }

    @Override
    public void preecherCampos(Grupo entidade) {
        fieldIdentificador.setText(entidade != null && entidade.getIdGrupo() != null ? entidade.getIdGrupo().toString() : null);
        fieldDescricao.setText(entidade != null ? entidade.getDescricao() : null);

    }

    @Override
    public void preecherEntidade(Grupo entidade) throws EJDLogicException {
        entidade.setDescricao(fieldDescricao.getText());
        entidade.setCodigo(grupoEJB.findMaiorIdentificador() != null ? grupoEJB.findMaiorIdentificador().getIdGrupo()+1 : 1);
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
        fieldDescricao.setEnabled(habilita);

    }

    @Override
    public ITableModelEntidade<Grupo> getTableModelEntidade() {

        return new ITableModelEntidade<Grupo>() {
            @Override
            public Object getCampoEntidade(Grupo entidade, int coluna) {
                switch (coluna) {
                case 0:
                    return entidade.getCodigo();
                case 1:
                    return entidade.getDescricao();
                }
                return null;
            }

            @Override
            public List<Grupo> getEntidades() {
                return dao.localizarTodos();
            }

            @Override
            public String[] getNomesColunas() {
                return new String[] { "Código", "Descrição"};
            }

        };
    }

    @Override
    public void validarCamposObrigatorios(Grupo entidade) throws EJDLogicException {
        ValidateEntity validateEntity = new ValidateEntity();

        validateEntity.validateNotNull(entidade.getDescricao(), "Descrição");

        validateEntity.doValidate();

    }
//
//    private void marcarCamposObrigatorios(EJDLabel... labels){
//
//        for (EJDLabel ejdLabel : labels) {
//            ejdLabel = ejdLabel.marcarCamposObrigatorios(true);
//
//        }
//
//    }

}
