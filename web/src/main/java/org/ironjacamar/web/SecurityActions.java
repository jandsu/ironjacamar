/*
 * IronJacamar, a Java EE Connector Architecture implementation
 * Copyright 2016, Red Hat Inc, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the Eclipse Public License 1.0 as
 * published by the Free Software Foundation.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse
 * Public License for more details.
 *
 * You should have received a copy of the Eclipse Public License 
 * along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.ironjacamar.web;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

import com.github.fungal.api.Kernel;

import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Privileged Blocks
 * @author <a href="mailto:jesper.pedersen@ironjacamar.org">Jesper Pedersen</a>
 */
class SecurityActions
{ 
   /**
    * Constructor
    */
   private SecurityActions()
   {
   }

   /**
    * Get the thread context class loader
    * @return The class loader
    */
   static ClassLoader getThreadContextClassLoader()
   {
      return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() 
      {
         public ClassLoader run()
         {
            return Thread.currentThread().getContextClassLoader();
         }
      });
   }

   /**
    * Set the thread context class loader
    * @param cl The class loader
    */
   static void setThreadContextClassLoader(final ClassLoader cl)
   {
      AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() 
      {
         public ClassLoader run()
         {
            Thread.currentThread().setContextClassLoader(cl);
            return null;
         }
      });
   }

   /**
    * Get a system property
    * @param name The property name
    * @return The property value
    */
   static String getSystemProperty(final String name)
   {
      return AccessController.doPrivileged(new PrivilegedAction<String>() 
      {
         public String run()
         {
            return System.getProperty(name);
         }
      });
   }

   /**
    * Set a system property
    * @param name The property name
    * @param value The property value
    */
   static void setSystemProperty(final String name, final String value)
   {
      AccessController.doPrivileged(new PrivilegedAction<Boolean>() 
      {
         public Boolean run()
         {
            System.setProperty(name, value);
            return Boolean.TRUE;
         }
      });
   }

   /**
    * Create an URLClassLoader
    * @param urls The urls
    * @param parent The parent class loader
    * @return The class loader
    */
   static URLClassLoader createURLCLassLoader(final URL[] urls, final ClassLoader parent)
   {
      return AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() 
      {
         public URLClassLoader run()
         {
            return new URLClassLoader(urls, parent);
         }
      });
   }

   /**
    * Create a WARClassLoader
    * @param kernel The kernel
    * @param parent The parent class loader
    * @return The class loader
    */
   static WARClassLoader createWARClassLoader(final Kernel kernel, final ClassLoader parent)
   {
      return AccessController.doPrivileged(new PrivilegedAction<WARClassLoader>() 
      {
         public WARClassLoader run()
         {
            return new WARClassLoader(kernel, parent);
         }
      });
   }

   /**
    * Create a WebClassLoader
    * @param cl The classloader
    * @param wac The web app context
    * @return The class loader
    */
   static WebAppClassLoader createWebAppClassLoader(final ClassLoader cl, final WebAppContext wac)
   {
      return AccessController.doPrivileged(new PrivilegedAction<WebAppClassLoader>() 
      {
         public WebAppClassLoader run()
         {
            try
            {
               return new WebAppClassLoader(cl, wac);
            }
            catch (IOException ioe)
            {
               return null;
            }
         }
      });
   }
}
