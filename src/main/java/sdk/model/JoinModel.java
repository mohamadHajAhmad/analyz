package sdk.model;

import java.util.List;
import java.util.Map;

public class JoinModel  extends AbstractModel{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String joinMapped;
    private String joinType;

    private List <JoinDataModel> joinDataModel;


    public JoinModel(String joinMapped, String joinType) {
        this.joinMapped = joinMapped;
        this.joinType = joinType;
    }


    public String getJoinMapped() {
        return joinMapped;
    }

    public void setJoinMapped(String joinMapped) {
        this.joinMapped = joinMapped;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public List<JoinDataModel> getJoinDataModel() {
        return joinDataModel;
    }

    public void setJoinDataModel(List<JoinDataModel> joinDataModel) {
        this.joinDataModel = joinDataModel;
    }
}
