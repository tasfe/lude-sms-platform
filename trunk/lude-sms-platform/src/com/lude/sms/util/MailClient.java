package com.lude.sms.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 发送邮件
 * @author island
 *
 */
public class MailClient
{
    public static boolean sendmail(String sendermail, String receivermail, String subject, String body) throws IOException
    {
        return sendmail(sendermail, receivermail, "", "", subject, body, false, null);
    }

    public static boolean sendmail(String sendermail, String receivermail, String subject, String body, boolean attachfile, Vector<File> attach) throws IOException
    {
        return sendmail(sendermail, receivermail, "", "", subject, body, attachfile, attach);
    }

    public static boolean sendmail(String sendermail, String receivermail, String cc, String subject, String body, boolean attachfile, Vector<File> attach) throws IOException
    {
        return sendmail(sendermail, receivermail, cc, "", subject, body, attachfile, attach);
    }

    public static boolean sendmail(String sendermail, String receivermail, String cc, String bcc, String subject, String body) throws IOException
    {
        return sendmail(sendermail, receivermail, cc, bcc, subject, body, false, null);
    }

    public static boolean sendmail(String sendermail, String receivermail, String cc, String bcc, String subject, String body, boolean attachfile, Vector<File> attach) throws IOException
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "127.0.0.1");

        Session session = Session.getDefaultInstance(props, null);
        try
        {
            Message msg = new MimeMessage(session);
            msg.setHeader("Content-Type", "text/html; charset=iso-8859-1");
            msg.setHeader("Content-Transfer-Encoding", "7bit");
            msg.setFrom(new InternetAddress(sendermail));

            InternetAddress[] address = InternetAddress.parse(receivermail);
            msg.setRecipients(Message.RecipientType.TO, address);
            if (!cc.equals(""))
            {
                InternetAddress[] ccaddress = InternetAddress.parse(cc);
                msg.setRecipients(Message.RecipientType.CC, ccaddress);
            }
            if (!bcc.equals(""))
            {
                InternetAddress[] bccaddress = { new InternetAddress(bcc) };
                msg.setRecipients(Message.RecipientType.BCC, bccaddress);
            }
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            if (attachfile)
            {
                Multipart mp = new MimeMultipart();

                MimeBodyPart msgPart = new MimeBodyPart();
                msgPart.setDisposition("inline");
                msgPart.setContent(body, "text/html");
                mp.addBodyPart(msgPart);

                for (int i = 0; i < attach.size(); i++)
                {
                    MimeBodyPart filePart = new MimeBodyPart();
                    File file = attach.elementAt(i);
                    FileDataSource fds = new FileDataSource(file);
                    DataHandler dh = new DataHandler(fds);
                    filePart.setFileName(file.getName());
                    filePart.setDisposition("attachment");
                    filePart.setDescription("Attached file: " + file.getName());
                    filePart.setDataHandler(dh);
                    mp.addBodyPart(filePart);
                }

                msg.setContent(mp);
            }
            else
            {
                msg.setContent(body, "text/html");
            }
            Transport.send(msg);
            return true;
        }
        catch (MessagingException mex)
        {
            System.out.println("\n--Exception handling in msgsendsample.java");

            mex.printStackTrace();
            System.out.println();
            Exception ex = mex;
            do
            {
                if ((ex instanceof SendFailedException))
                {
                    SendFailedException sfex = ( SendFailedException ) ex;
                    Address[] invalid = sfex.getInvalidAddresses();
                    if (invalid != null)
                    {
                        System.out.println("    ** Invalid Addresses");
                        if (invalid != null)
                        {
                            for (int i = 0; i < invalid.length; i++)
                                System.out.println("         " + invalid[i]);
                        }
                    }
                    Address[] validUnsent = sfex.getValidUnsentAddresses();
                    if (validUnsent != null)
                    {
                        System.out.println("    ** ValidUnsent Addresses");
                        if (validUnsent != null)
                        {
                            for (int i = 0; i < validUnsent.length; i++)
                                System.out.println("         " + validUnsent[i]);
                        }
                    }
                    Address[] validSent = sfex.getValidSentAddresses();
                    if (validSent != null)
                    {
                        System.out.println("    ** ValidSent Addresses");
                        if (validSent != null)
                        {
                            for (int i = 0; i < validSent.length; i++)
                                System.out.println("         " + validSent[i]);
                        }
                    }
                }
                System.out.println();
                if ((ex instanceof MessagingException))
                    ex = (( MessagingException ) ex).getNextException();
                else
                    ex = null;
            }
            while (ex != null);
        }
        return false;
    }
}
