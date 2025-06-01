$(function() {
    const $tabButtons = $('#templateTabs button[data-bs-toggle="tab"]');
    const tabButtons = Array.from($tabButtons);

    tabButtons.forEach(button => {
        button.addEventListener('click', function(event) {
            event.preventDefault();
            const tab = new bootstrap.Tab(this);
            tab.show();
        });
    });
});

// Controlador devuelve fragmento
@GetMapping("/api/tab/usuarios")
public String getUsuariosTab(Model model) {
    model.addAttribute("usuarios", usuarioService.findAll());
    return "fragments/usuarios-tab :: usuariosContent";
}


// Frontend inserta HTML directamente
$.get('/api/tab/usuarios')
 .done(function(html) {
     $('#usuarios').html(html);
 });