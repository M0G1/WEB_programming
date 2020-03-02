package order;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface OrderListInterface extends List<Order>, Serializable, Remote {

    public void sortAndSaveUnique() throws RemoteException;
}
