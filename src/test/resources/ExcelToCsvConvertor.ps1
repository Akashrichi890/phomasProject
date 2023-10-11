Function Global:Convert-CsvInBatch
{
    Param
    (
        [Parameter(Mandatory=$true)]
        [ValidateNotNull()]
        [ValidateNotNullorEmpty()]
        [String]
        $Folder,
        [Parameter(Mandatory=$true)]
        [ValidateNotNull()]
        [ValidateNotNullorEmpty()]
        [String]
        $Delimiter,
        [String]
        $ArchiveFL
    )

    $lastCharacter = $Folder.Substring($Folder.Length-1,1)

    if($lastCharacter -eq "\" -or $lastCharacter -eq "/")
    {
        $Folder =  $Folder.Substring(0,$Folder.Length-1)
    }

    
    $pathWithFolder = $Folder

    $folderExist = Test-Path $pathWithFolder
    
    if($folderExist -eq $true)
    {
        $isPathRooted = [System.IO.Path]::IsPathRooted($Folder)
        if($isPathRooted -eq $false)
        {
            $path = gl
            $pathWithFolder = "$path\$Folder"        
            $folderExist = Test-Path $pathWithFolder            
        }
        else
        {
            $pathWithFolder = $pathWithFolder.Replace("/", "\")
        }
    }
    else
    {
        Write-Host "Given folder doesn't exist" -ForegroundColor Red
    }


    If($ArchiveFL.Length -gt 0)
    {
        $lastCharacter = $ArchiveFL.Substring($ArchiveFL.Length-1,1)
    
        if($lastCharacter -eq "\" -or $lastCharacter -eq "/")
        {
            $ArchiveFL =  $ArchiveFL.Substring(0,$ArchiveFL.Length-1)
        }
        $archiveFolderExist = Test-Path $ArchiveFL
        if($archiveFolderExist -eq $false)
        {
            $isPathRooted = [System.IO.Path]::IsPathRooted($ArchiveFL)
            if($isPathRooted -eq $false)
            {
                $ArchiveFL = "$pathWithFolder\$ArchiveFL"       
            }

            $archiveFolderExist = Test-Path $ArchiveFL
            if($archiveFolderExist -eq $false)
            {
                md $ArchiveFL
            }

            $ArchiveFL = $ArchiveFL.Replace("/", "\")
        }
    
        $archiveFolder = $ArchiveFL

        $todayDate = Get-Date -Format "yyyyMMdd"
        $ArchiveFL = "$ArchiveFL\$todayDate"

        $archiveFolderExist = Test-Path $ArchiveFL

        if($archiveFolderExist -eq $false)
        {
            md $ArchiveFL
        }
    }

    
    If($folderExist -eq $true)
    {
        $ExcelFiles = Get-ChildItem -Path $pathWithFolder -Recurse -Filter *.xlsx |Select-Object fullname,name,directory
        $Excel = New-Object -ComObject Excel.Application

        ForEach($ExcelFile in $ExcelFiles)
        {
            
            If($ArchiveFL.Length -gt 0)
            {
                $isItArchiveFolderFiles =  $ExcelFile.Directory -match [Regex]::Escape($archiveFolder)
                if($isItArchiveFolderFiles -eq $true) {
                    continue;
                }
            }
            
            $directory = $ExcelFile.Directory -replace [Regex]::Escape($pathWithFolder)
            
            $insideCsvFilePath = $ExcelFile.Directory

            $fileName = $ExcelFile.Name -replace "\.xlsx$"
            $xlsFilePath = $ExcelFile.FullName
        
            $wb = $Excel.Workbooks.Open($xlsFilePath)
	
            $sheetCount = $wb.Worksheets.count

            if($sheetCount -eq 1)
            {
                $completeFileName = "$insideCsvFilePath\$fileName.csv"
                $wb.Worksheets.Item(1).SaveAs("$insideCsvFilePath\$fileName.csv", [Microsoft.Office.Interop.Excel.XlFileFormat]::xlUnicodeText);
                $completeFileName1 = "$insideCsvFilePath\$fileName-1-2-3-.csv"                
                Import-CSV $completeFileName -Delimiter "`t" | ConvertTo-CSV -NoTypeInformation -Delimiter $Delimiter | Out-File $completeFileName1 -Encoding utf8
            }
            else
            {
                foreach ($ws in $wb.Worksheets) 
                {
                    $sheetName = $ws.Name -replace "\W"
                    $completeFileName = "$insideCsvFilePath\$fileName-$sheetName.csv"
                    $ws.SaveAs($completeFileName, [Microsoft.Office.Interop.Excel.XlFileFormat]::xlUnicodeText)
                    $completeFileName1 = "$insideCsvFilePath\$fileName-$sheetName-1-2-3-.csv"                
                    Import-CSV $completeFileName -Delimiter "`t" | ConvertTo-CSV -NoTypeInformation -Delimiter $Delimiter | Out-File $completeFileName1 -Encoding utf8
                }
            } 

            $wb.close();

            If($ArchiveFL.Length -gt 0)
            {
                $subArchiveFolderExist = Test-Path "$ArchiveFL$directory"

                if($subArchiveFolderExist -eq $false)
                {
                    md "$ArchiveFL\$directory"
                }

            
                #move/overwrite file in archive folder
                $fileName = $ArchiveFL+$directory+"\"+$fileName+".xlsx"
                $archiveFLFileExists = Test-Path $fileName
                if($archiveFLFileExists -eq $true)
                {
                    Remove-Item -Path $fileName -Force
                }                
                Move-Item -Path $ExcelFile.FullName $fileName -Force
            }
        }

        $Excel.Quit()
        [System.GC]::Collect()
        [System.GC]::WaitForPendingFinalizers()
        [System.Runtime.Interopservices.Marshal]::ReleaseComObject($Excel)

        $insideCsvFilePath = "$pathWithFolder"

        $DefaultFiles =  Get-ChildItem $insideCsvFilePath -Recurse | Where-Object {$_.Name -like "*-1-2-3-.csv"} |Select-Object fullname 
    
        ForEach($File in $DefaultFiles) 
        {
            $newName = ([String]$File.FullName).Replace("-1-2-3-","")
            Remove-Item -Path $newName -Force
            Move-item -Path $File.FullName $newName -Force
        }        
    }
 }

 # with "archive" folder
 # Convert-CsvInBatch -Folder 'D:\downloads\1' -Delimiter ';' -ArchiveFL '_archive'
 
 # without generate "archive" folder
 # Convert-CsvInBatch -Folder 'G:\excel-files' -Delimiter ','