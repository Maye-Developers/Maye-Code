<?php
require_once("baglan.php");//database bağlantısı gercekleştirdik
if($_POST){//eğer posttan geliyorsa işlem yapacak
   $username = $_POST["user"];//kullanıcı adını aldık
   $sifre = $_POST["sifre"];//sifreyi aldık

   //mail ve şifreyi androidde kontrol etmiştik .Güvenlik acısından burdada kontrol edeceğiz.
   $hata = false;
   $sonucmesaji = "";

   if($username==""){//mail bos mu kontrolu
       $hata = true;
       $sonucmesaji = "Mail adresiniz yada kullanıcı adınız boş olamaz";
   }
   if(strlen($sifre)<6){//şifre 6 haneden kısa mı kontrolu
     $hata =true;
     $sonucmesaji = "Şifre 6 Haneden kısa olamaz.";
   }
   if(!$hata){//eğer hata yoksa database sorgusu yapılacak
       $eskisifre = $sifre;
       $sifre = md5($sifre);
       $sql ="SELECT * FROM Kullanici WHERE mail='$username' OR kadi='$username' AND sifre='$sifre'";//mail ve şifre kontrolü
       $sonuc = mysqli_query($con,$sql);
       if(mysqli_num_rows($sonuc)>0){//0 dan fazla veri varsa
           $oku = mysqli_fetch_assoc($sonuc);
           $sonucmesaji = "Giris Basarili.";

           $idi = $oku["id"];
           $kadi = $oku["kadi"];
           $mail = $oku["mail"];
           $soru = $oku["soru"];

           $sql = "SELECT adimId FROM KullaniciAdim Where kulId =".$oku["id"];
           $sonuc = mysqli_query($con,$sql);
           $oku = mysqli_fetch_assoc($sonuc);

           $sql = "SELECT id FROM Adim Where id =".$oku["adimId"];
           $sonuc = mysqli_query($con,$sql);
           $oku = mysqli_fetch_assoc($sonuc);

           $cevap = array('sonuc' => "1",'id' => $idi,'soru' => $soru,'sifre' => $eskisifre, 'sonucmesaji' => $sonucmesaji, 'kullaniciadi' => $kadi, 'mail' => $mail, 'adim' => $oku["id"]);
       }else{
           $sonucmesaji = "Kullanıcı Bulunamadı.";
           $cevap = array('sonuc' => "0", 'sonucmesaji' =>  $sonucmesaji);
       }
       mysqli_close($con);//database bağlantısını kapattık

   }else{
       $cevap = array('sonuc' => "0",'sonucmesaji' => $sonucmesaji);
   }
   echo json_encode($cevap);// json verisini yazdırdık
}else{
  echo "Giriş Engellendi";
}
?>
