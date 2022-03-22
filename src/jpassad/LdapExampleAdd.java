package jpassad;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

public class LdapExampleAdd
{
    private static DirContext ctx = null;

    public LdapExampleAdd()
    {
    }

    /**
     * Generic method to obtain a reference to a DirContext
     * 
     * @param ldapHostName
     * @param ldapPost
     * @param bindDn
     * @param bindDnPwd
     */
    public static DirContext getDirContext(String ldapHostName, String ldapPost, String bindDn, String bindDnPwd) throws Exception
    {
	Hashtable<String, String> env = new Hashtable<String, String>(11);
	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	env.put(Context.PROVIDER_URL, "ldap://" + ldapHostName + ":" + ldapPost);
	env.put(Context.SECURITY_PRINCIPAL, bindDn);
	env.put(Context.SECURITY_CREDENTIALS, bindDnPwd);
	// Create the initial context
	ctx = new InitialDirContext(env);
	return ctx;
    }

    void createLDAPEntry(String[] args)
    {
	System.out.println("Performing LDAP Add with:");
	System.out.println("	ldapHostName = " + args[0]);
	System.out.println("	    ldapPort = " + args[1]);
	System.out.println("	      bindDn = " + args[2]);
	System.out.println("	   bindDnPwd = " + args[3]);
	System.out.println("	  	Base = " + args[4]);
	System.out.println("	      	 uid = " + args[5]);
	System.out.println("       givenname = " + args[6]);
	System.out.println("		  sn = " + args[7]);
	System.out.println("   userPassword: = " + args[8]);
	try
	{// Bind as a user which can create the entry
	    ctx = getDirContext(args[0], args[1], args[2], args[3]);
	}
	catch (javax.naming.AuthenticationException e)
	{
	    System.err.println("Could not authenticate as:  "+  args[2] + " With the password: "+ args[3] + "\n"+ e);
	    System.exit(49);
	}
	catch (Exception e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// Add the required and optional attributes to the Entry
	BasicAttributes matchAttrs = new BasicAttributes(true);
	matchAttrs.put(new BasicAttribute("uid", args[5]));
	matchAttrs.put(new BasicAttribute("cn", args[5]));
	matchAttrs.put(new BasicAttribute("givenname", args[6]));
	matchAttrs.put(new BasicAttribute("sn", args[7]));
	matchAttrs.put(new BasicAttribute("userpassword", args[8]));
	matchAttrs.put(new BasicAttribute("objectclass", "top"));
	matchAttrs.put(new BasicAttribute("objectclass", "person"));
	matchAttrs.put(new BasicAttribute("objectclass", "organizationalPerson"));
	matchAttrs.put(new BasicAttribute("objectclass", "inetorgperson"));
	// Construct the Fully Distinguished Name
	String name = "uid=" + args[5] + "," + args[4];
	InitialDirContext iniDirContext = (InitialDirContext) ctx;
	try
	{// Create the entry within LDAP
	    iniDirContext.bind(name, ctx, matchAttrs);
	}
	catch (NameAlreadyBoundException e)
	{
	    System.err.println("The entry: "+ name + " Already exists!"+ "\n"+ e);
	    System.exit(68);
	}
	catch (javax.naming.NameNotFoundException e)
	{
	    System.err.println("The Container "+  args[4] + " Probably does not exist!"+ "\n"+ e);
	    System.exit(32);
	}
	catch (NamingException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	try
	{
	    iniDirContext.close();
	}
	catch (NamingException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	System.out.println("Successfully created LDAP Entry:" + name);
    }

    /**
     * Does a simple search on the LDAP Directory
     * 
     * String ldapHostName = args[0]; String ldapPort = args[1]; String bindDn = args[2]; String bindDnPwd = args[3]; String base = args[4] String uid = args[5]; String givenname=args[6]; String sn = args[7];
     * String password = args[8];
     * 
     * @param args
     * 
     */
    public static void main(String[] args)
    {
	String[] argsNames = { "ldapHostName", "ldapPort", "bindDn", "bindDnPwd", "base" };
	if (args.length == 9)
	{
	    LdapExampleAdd basicjndiadd = new LdapExampleAdd();
	    basicjndiadd.createLDAPEntry(args);
	}
	else
	{
	    System.out.println("\nYou must provide: ");
	    for (int i = 0; i < argsNames.length; i++)
	    {
		System.out.print(argsNames[i] + "  ");
	    }
	    System.out.print("\non the command line!");
	}
    }
}