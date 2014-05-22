package com.espendwise.manta.util;

import java.util.List;
import java.io.Serializable;


public class UpdateRequest<T> implements Serializable {

    List<T> toCreate;
    List<T> toUpdate;
    List<T> toDelete;

    public UpdateRequest() {
    }

    public UpdateRequest(List<T> toCreate, List<T> toUpdate, List<T> toDelete) {
        this.toCreate = toCreate;
        this.toUpdate = toUpdate;
        this.toDelete = toDelete;
    }

    public List<T> getToCreate() {
        return toCreate;
    }

    public void setToCreate(List<T> toCreate) {
        this.toCreate = toCreate;
    }

    public List<T> getToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(List<T> toUpdate) {
        this.toUpdate = toUpdate;
    }

    public List<T> getToDelete() {
        return toDelete;
    }

    public void setToDelete(List<T> toDelete) {
        this.toDelete = toDelete;
    }



    @Override
    public String toString() {
        return "UpdateRequest{" +
                "toCreate=" + toCreate +
                ", toUpdate=" + toUpdate +
                ", toDelete=" + toDelete +
                '}';
    }
}
