package etu2011.framework.servlet;

import jakarta.servlet.http.HttpServletRequest;
import orm.database.object.relation.ModelField;
import orm.database.object.relation.Relation;
import orm.utilities.Treatment;

public abstract class ModelController<T> extends Relation<T> {

    /* CONSTRUCTOR SECTION */
    public ModelController() throws Exception {
        super();
    }

    /* METHODS SECTION */
    public void initModel(HttpServletRequest req) throws Exception {
        ModelField[] fields = this.getColumn();
        for (int i = 0; i < fields.length; i++) {
            Treatment.setObjectFieldValue(this, req.getParameter(fields[i].getOriginalName()), fields[i]);
        }
    }

}
