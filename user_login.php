<?php
if ($_SERVER['REQUEST_METHOD']=='POST') {
    include 'DatabaseConfig.php';
    $con = mysqli_connect($HostName,$HostUser,$HostPass,$DatabaseName);
    $Email = $_POST['User_Email'];
    $Password = $_POST['User_Password'];
    $sql_Query = "SELECT * FROM `tbuser` WHERE User_Email = '$Email' AND User_Password = '$Password'";
    $check = mysqli_fetch_array(mysqli_query($con, $sql_Query));
    if (isset($check)) {
        echo "Data Anda benar";
    }
    else {
        echo "Username dan Password salah";
    }
}
else {
    echo "Cek kembali";
}
mysqli_close($con);
?>