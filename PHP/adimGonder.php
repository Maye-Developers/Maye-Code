<?php
@header('Content-type: application/json; charset=utf-8');
require_once("baglan.php");
mysql_query("SET NAMES 'latin5'");
mysql_query("SET CHARACTER SET latin5");
mysql_query("SET COLLATION_CONNECTION = 'latin5_turkish_ci'");
if($_POST){
	$id = $_POST["id"];
	$ses = $_POST["session"];
	$yenisayfa = $_POST["yenisayfa"];

	$hata = false;
	$sonucmesaji = "";
	$cevap="";
	$yazi='';
	$i=0;
	$yeniNo = $id;

	if($id==""){
		$hata=true;
		$sonucmesaji="id Boş";
	}
	if(!$hata){
		while (true) {
				$yeniNo = $yeniNo+1;
				$sql="SELECT * FROM Adim WHERE id=".$yeniNo;
				$sonuc=mysqli_query($con,$sql);
				if(mysqli_num_rows($sonuc)<1){
					continue;
				}
				else {
							$sql="SELECT * FROM Adim WHERE kategoriId=1";
							$sonuc=mysqli_query($con,$sql);
							$toplam = mysqli_num_rows($sonuc);


							$sql="SELECT * FROM Adim WHERE id=".$yeniNo;
							$sonuc=mysqli_query($con,$sql);
							if(mysqli_num_rows($sonuc)>0){
								$oku=mysqli_fetch_assoc($sonuc);

								$baslik =$oku["baslik"];
								$icerik =$oku["icerik"];
								$kategoriId =$oku["kategoriId"];

								$sql="SELECT * FROM Parca WHERE Adim_id=".$yeniNo;
								$sonuc=mysqli_query($con,$sql);
								if(mysqli_num_rows($sonuc)>0){
									while($oku=mysqli_fetch_assoc($sonuc)){
										$parcayazi[$i]=$oku["Adi"];
										$i++;
									}
									for ($i=0; $i < sizeof($parcayazi); $i++) {
											if($i<(sizeof($parcayazi)-1)){
												$yazi = $yazi.'{"adi":"'.$parcayazi[$i].'"},';
											}
											else {
												$yazi = $yazi.'{"adi":"'.$parcayazi[$i].'"}';
											}
									}

									$sql="UPDATE Kullanici SET soru=".$yeniNo." WHERE id=".$ses;
									$sonuc=mysqli_query($con,$sql);

									$sql="SELECT sayac FROM Kullanici WHERE id=".$ses;
									$sonuc=mysqli_query($con,$sql);
									$oku = mysqli_fetch_assoc($sonuc);
									$sayac = $oku["sayac"];

									if($yenisayfa == "t"){
										$sayac++;

										$sql="UPDATE Kullanici SET sayac=".$sayac." WHERE id=".$ses;
										$sonuc=mysqli_query($con,$sql);
									}

									$jsonolustur = '{"sonuc":"1","sonucmesaji":"'.$sonucmesaji.'","sayac" : "'.$sayac.'","id":"'.$yeniNo.'","baslik":"'.$baslik.'","icerik":"'.$icerik.'","kategoriId":"'.$kategoriId.'","toplam":"'.$toplam.'","parca":['.$yazi.']}';
								//$cevap =array('sonuc'=>"1",'sonucmesaji'=>$sonucmesaji,'id'=>$id, 'baslik'=>$baslik,'icerik'=>$icerik,'kategoriId'=>$kategoriId, 'ert' => array('etiket' => "<a", ""));

									mysqli_close($con);
									echo $jsonolustur;// json verisini yazdırdık
								}

								else{
									$sonucmesaji="soru bulunamadi";
								}
							}
							else{
								echo "hata";
							}
							break;
				}
		}


}else{
	echo "hata";
}
}else {
	echo "hata";
}



?>
