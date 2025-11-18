package main.java.facade;

import java.util.List;

import main.java.mgr.*;

//아직은 사용하지 않음. 추후에 사용예정
public abstract class DataEngineImpl<T extends Manageable> implements IDataEngine<T> {

    protected Manager<T> mgr=new Manager<>();

    public void setManager(Manager<T> mgr){
        this.mgr=mgr;
    }

    @Override
    public List<T> search(String kwd) {
        if(kwd==null)return mgr.mList;
        return mgr.findAll(kwd);
    }

    
    @Override
    public void update(Object[] uiData) {
        Manageable s=mgr.find((String)uiData[0]);
        ((UIData)s).set(uiData);
    }

    @Override
    public void remove(String kwd) {
        Manageable s=mgr.find(kwd);
        mgr.mList.remove(s);
    }
    
}
