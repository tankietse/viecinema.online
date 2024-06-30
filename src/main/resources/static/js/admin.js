document.addEventListener("DOMContentLoaded", function() {
    const menuLinks = document.querySelectorAll('.sidebar a');

    menuLinks.forEach(link => {
        link.addEventListener('click', function() {
            const text = this.textContent.trim();
            document.getElementById('breadcrumb').textContent = `Dashboard/${text}`;
        });
    });
});


