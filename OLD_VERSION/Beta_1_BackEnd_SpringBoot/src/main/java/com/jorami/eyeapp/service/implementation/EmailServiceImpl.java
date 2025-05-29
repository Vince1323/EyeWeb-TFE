package com.jorami.eyeapp.service.implementation;

import com.jorami.eyeapp.model.User;
import com.jorami.eyeapp.auth.model.VerificationCode;
import com.jorami.eyeapp.repository.VerificationCodeRepository;
import com.jorami.eyeapp.service.EmailService;
import com.jorami.eyeapp.strategy.EmailStrategy;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Calendar;


@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public void sendEmail(User user, String code, EmailStrategy emailStrategy) {
        try {
            this.invalidCertificate();
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Commentaire fonctionne potentiellement sur AWS
            //ClassPathResource filePath = new ClassPathResource("light.txt");
            //String filePath = "src/main/resources/light.txt";
            //String base64Image = new String(Files.readAllBytes(Paths.get(filePath.getURI())), StandardCharsets.UTF_8);

            //ClassPathResource resource = new ClassPathResource("template/registration_template.html");
            //String content = new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);

            String content = emailStrategy.prepareEmailContent(user, code);

            helper.setText(content, true);
            helper.setTo(user.getEmail());
            helper.setSubject(emailStrategy.getSubject());
            helper.setFrom("noreply@eyeweb.app", "EyeWeb");

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new IllegalStateException("failed to send email");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("failed to send email");
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Valide l'existence d'un code de vérification et vérifie sa validité temporelle.
     * Cette méthode recherche un code de vérification par sa valeur et détermine si ce dernier
     * est toujours valide en fonction de son heure d'expiration. Si le code est trouvé mais qu'il a expiré,
     * il est supprimé de la base de données et la méthode retourne null. Si le code est valide et n'a pas encore expiré,
     * l'objet {@link VerificationCode} correspondant est retourné.
     * @param code Le code de vérification à valider.
     * @return L'objet {@link VerificationCode} si le code est valide et n'a pas expiré, sinon retourne null.
     */
    @Override
    public VerificationCode validateCodeExist(String code) {
        VerificationCode verificationCode = verificationCodeRepository.findByCode(code);
        Calendar calendar = Calendar.getInstance();

        if (verificationCode != null && (verificationCode.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            verificationCodeRepository.delete(verificationCode);
            verificationCodeRepository.flush();
            verificationCode = null;
        }

        return verificationCode;
    }

    private void invalidCertificate() throws NoSuchAlgorithmException, KeyManagementException {
        // Créer un SSLContext qui ignore la validation du certificat
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[]{new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) {}
            public void checkServerTrusted(X509Certificate[] chain, String authType) {}
            public X509Certificate[] getAcceptedIssuers() { return null; }
        }}, new java.security.SecureRandom());

        // Appliquer le SSLContext à toutes les connexions SSL
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

}

