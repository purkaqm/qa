package ps5.wbs.test.beans;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.work.WorkStatus;

public class WorkBean extends WBSBean {

    private WorkStatus status;

    public WorkBean(PersistentKey id) {
        super(id);
    }

    public WorkStatus getStatus() {
        return status;
    }

    public void setStatus(WorkStatus status) {
        this.status = status;
    }
}
