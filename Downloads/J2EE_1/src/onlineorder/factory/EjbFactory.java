package onlineorder.factory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * @author qianzhihao
 * @version 2018/1/6
 */
public class EjbFactory {
    public static Object getEJB(String beanName, String interfaceName) {
        Hashtable<String, String> jndiProperties = new Hashtable<>();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        try {
            Context context = new InitialContext(jndiProperties);
            final String fullName = "ejb://j2ee-server//" + beanName + "!" + interfaceName;
            return context.lookup(fullName);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
