<?php
require_once("baglan.php");//database bağlantısı gercekleştirdik

if($_POST){//eğer posttan geliyorsa işlem yapacak
    $kadi = $_POST["kadi"];//isimi aldık
    $mail = $_POST["mail"];//mail adresini aldık
    $sifre = $_POST["sifre"];//sifreyi aldık

    $hata = false;
    $sonucmesaji = "";
    if($kadi==""){
        $hata = true;
        $sonucmesaji = "İsim Boş Olamaz.\n";
    }
    if($mail==""){//mail boş mu
        $hata = true;
        $sonucmesaji = "Mail Adresiniz Boş Olamaz.\n";
    }
    if(strlen($sifre)<6){
      $hata = true;
      $sonucmesaji = "Şifre 6 haneden az olamaz";
    }
    //Mailin önceden eklenip eklenmediğni kontrol ediyoruz. Çünkü mail unique olmalı.
    $sql ="SELECT * FROM Kullanici WHERE mail='".$mail."' OR kadi='".$kadi."'";
    $sonuc = mysqli_query($con,$sql);
    if(mysqli_num_rows($sonuc)>0){//0 dan fazla veri varsa
        $hata =true;
        $sonucmesaji = "Bu Mail Adresi İle Önceden Kayıt olunmuş.\n";
    }
    if (!filter_var($mail, FILTER_VALIDATE_EMAIL)) {//mail format kontrol
        $hata =true;
        $sonucmesaji = "Mail Formatı Yanlış.\n";
    }

    if(!$hata){//eğer hata yoksa
        $sifre = md5($sifre);
        $sql="INSERT INTO Kullanici (kadi, mail, sifre, kayit)
            VALUES
        ('$kadi','$mail','$sifre',NOW())";//NOW() fonksiyonu şimdiki zamanı alır

        if (!mysqli_query($con,$sql)){//Kaydedilemediyse
            $sonucmesaji = mysqli_error($con);//mysqln döndüğü hata mesajını aldık
            $cevap = array('sonuc' => "0", 'sonucmesaji' => $sonucmesaji);
        }else{//Kayıt Başarılı ise
            $sonucmesaji = "Kayıt Başarılı.";
            $cevap = array('sonuc' => "1", 'sonucmesaji' => $sonucmesaji);
        }
        mysqli_close($con);//database bağlantısını kapattık

    }else{//Eğer posttan gelen verilerde hata varsa calısacak
        $cevap = array('sonuc' => "0",'sonucmesaji' => $sonucmesaji);
    }
    echo json_encode($cevap);// json verisini yazdırdık
}else{
    echo "Giriş Engellendi";
}
?>
