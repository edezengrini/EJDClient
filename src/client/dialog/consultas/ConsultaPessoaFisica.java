package client.dialog.consultas;

import javax.inject.Inject;

import server.dao.PessoaFisicaDAO;
import server.ejb.PessoaFisicaEJB;
import server.entities.PessoaFisica;
import arquitetura.common.exception.EJDLogicException;
import arquitetura.common.exception.EnumEJDException;
import client.dialog.DialogCrud;
import client.dialog.DialogLocalizar;

public class ConsultaPessoaFisica {

    PessoaFisica pessoaFisica = new PessoaFisica();
    PessoaFisicaDAO pessoaFisicaDao = new PessoaFisicaDAO();

    @Inject
    private PessoaFisicaEJB pessoaFisicaEJB = new PessoaFisicaEJB();

    public PessoaFisica consultarPessoaFisica(DialogCrud<PessoaFisica> dialogPessoaFisica) throws EJDLogicException {

        DialogLocalizar<PessoaFisica> formLocalizar = new DialogLocalizar<PessoaFisica>(dialogPessoaFisica, pessoaFisicaDao.localizarTodos());
        formLocalizar.setVisible(true);

        PessoaFisica entidadeSelecionada = formLocalizar.getEntidadeSelecionada();

        if (entidadeSelecionada != null) {
            pessoaFisica = pessoaFisicaEJB.find(entidadeSelecionada.getIdPessoa());
        }

        if (pessoaFisica == null) {
            throw new EJDLogicException(EnumEJDException.ENTITY_FIND, "Pessoa Física");
        }

        return pessoaFisica;
    }

}
