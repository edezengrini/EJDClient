package client.dialog.cases;

import java.awt.Window;

import javax.swing.JComponent;

import server.dao.AbstractDAO;
import server.entities.Bairro;
import server.entities.abstracts.EntityState;
import arquitetura.common.exception.EJDLogicException;
import client.dialog.DialogCrud;
import client.table.TableModelEntidade.ITableModelEntidade;

public class DialogBairro extends DialogCrud<Bairro> {

    public DialogBairro(Window window) {
        super(window);
        // TODO Auto-generated constructor stub
    }

    private static final long serialVersionUID = 1L;

    @Override
    public AbstractDAO<Bairro> getDAO() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void preecherEntidade(Bairro entidade) throws EJDLogicException {
        // TODO Auto-generated method stub

    }

    @Override
    public void preecherCampos(Bairro entidade) {
        // TODO Auto-generated method stub

    }

    @Override
    public void habilitarCampos(EntityState state) {
        // TODO Auto-generated method stub

    }

    @Override
    public ITableModelEntidade<Bairro> getTableModelEntidade() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void validarCamposObrigatorios(Bairro entidade) throws EJDLogicException {
        // TODO Auto-generated method stub

    }

    @Override
    public JComponent getFirstFocusable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitulo() {
        // TODO Auto-generated method stub
        return null;
    }

}
