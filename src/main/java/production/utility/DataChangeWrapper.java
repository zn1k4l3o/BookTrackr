package production.utility;

import production.generics.DataChange;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataChangeWrapper implements Serializable {

    List<DataChange> dataChangeList;
    final Integer maxNumOfLogs = 100;

    public List<String> getStringifiedChanges() {
        List<String> changeList = new ArrayList<>();
        for (DataChange dataChange : dataChangeList) {
            changeList.add(dataChange.toString());
        }
        return changeList;
    }

    public DataChangeWrapper(List<DataChange> dataChangeList) {
        this.dataChangeList = dataChangeList;
    }

    public List<DataChange> getDataChangeList() {
        return dataChangeList;
    }

    public void setDataChangeList(List<DataChange> dataChangeList) {
        this.dataChangeList = dataChangeList;
    }

    //zove se u kombinaciji s FileUtils.readDataChangeFromFile
    public void loadData(DataChangeWrapper oldDataChangeWrapper) {
        this.dataChangeList = oldDataChangeWrapper.dataChangeList;
    }

    public void loadPreDefinedPath() {
        DataChangeWrapper oldDataChangeWrapper = FileUtils.readDataChangeFromFile();
        this.dataChangeList = oldDataChangeWrapper.dataChangeList;
    }

    public void addDataChange(DataChange dataChange) {
        //loadPreDefinedPath();
        dataChangeList.add(dataChange);
        if (dataChangeList.size() > maxNumOfLogs) dataChangeList.removeFirst();
        FileUtils.writeDataChangeToFile(this);
    }
}
