package ps5.wbs.test.beans;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.entities.ResourcePool;
import com.cinteractive.ps3.entities.User;

public class ResourceBean extends WBSBean {

    private ResourcePool pool;

    private User user;

    public ResourceBean(PersistentKey id) {
        super(id);
    }

    public ResourcePool getPool() {
        return pool;
    }

    public User getUser() {
        return user;
    }

    public void setPool(ResourcePool pool) {
        this.pool = pool;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // !!!!
    @Override
    public String toString() {
        return String.format("%s: user=%s, pool=%s", getName(), user == null ? "" : user.getName(), pool == null ? ""
                : pool.getName());
    }
}
