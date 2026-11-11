$folders = @(
    "docs",
    "android/gradle/wrapper",
    "android/app/src/main/res/values",
    "android/app/src/debug/res/xml",
    "android/app/src/main/java/com/bookease/core/network",
    "android/app/src/main/java/com/bookease/core/security",
    "android/app/src/main/java/com/bookease/core/navigation",
    "android/app/src/main/java/com/bookease/data/models",
    "android/app/src/main/java/com/bookease/data/repositories",
    "android/app/src/main/java/com/bookease/domain/repositories",
    "android/app/src/main/java/com/bookease/features/auth",
    "android/app/src/main/java/com/bookease/features/home",
    "android/app/src/main/java/com/bookease/ui/theme",
    "android/app/src/test/java/com/bookease",
    "backend/gradle/wrapper",
    "backend/src/main/resources",
    "backend/src/main/kotlin/com/bookease/config",
    "backend/src/main/kotlin/com/bookease/security",
    "backend/src/main/kotlin/com/bookease/auth",
    "backend/src/main/kotlin/com/bookease/users",
    "backend/src/main/kotlin/com/bookease/common",
    "backend/src/test/kotlin/com/bookease"
)

foreach ($f in $folders) {
    New-Item -ItemType Directory -Force -Path $f | Out-Null
}

Write-Host "Folder structure created successfully!"
