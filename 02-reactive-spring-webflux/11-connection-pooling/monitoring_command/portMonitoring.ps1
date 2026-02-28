while ($true) {
    Clear-Host
    Write-Host "Monitoring port 7070 on 127.0.0.1 (refreshes every 2s)...`n"
    netstat -an | findstr "127.0.0.1:7070"
    Start-Sleep -Seconds 2
}