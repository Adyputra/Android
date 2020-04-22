<?php
if ($_SERVER['REQUEST_METHOD']=='POST') {
    include 'DatabaseConfig.php';
    $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
    $Email = $_POST['User_Email'];
    $Password = $_POST['User_Password'];
    $Full_Name = $_POST['User_Full_Name'];
    $CheckSQL = "SELECT * FROM `tbuser` WHERE User_Email = '$Email'";
    $check = mysqli_fetch_array(mysqli_query($con, $CheckSQL));
    if (isset($check)) {
        echo 'Email sudah tercantum, dimohon memakai email lain';
    } 
    else {
       $sql_query = "INSERT INTO `tbuser` (`id`, `User_Email`, `User_Password`, `User_Full_Name`) VALUES (NULL, '$Email', '$Password', '$Full_Name')";
    if (mysqli_query($con, $sql_query)) {
        echo 'Registrasi berhasil';
    }
    else {
        echo 'Registrasi Gagal';
    }
    }
}
mysqli_close($con);
?>