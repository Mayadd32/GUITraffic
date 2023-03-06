{ pkgs }: {
    deps = [
        pkgs.q-text-as-data
        pkgs.graalvm17-ce
        pkgs.maven
        pkgs.replitPackages.jdt-language-server
        pkgs.replitPackages.java-debug
    ];
}