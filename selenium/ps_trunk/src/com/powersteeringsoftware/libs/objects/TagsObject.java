package com.powersteeringsoftware.libs.objects;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 16:44
 * To change this template use File | Settings | File Templates.
 */
public interface TagsObject {

    List<PSTag> getTags();

    void setTags(PSTag... tags);

    void addTag(PSTag tg);

    void setTags(List<PSTag> tags);
}
