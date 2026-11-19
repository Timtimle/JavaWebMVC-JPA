document.querySelectorAll('form[data-validate]').forEach(function (form) {
    form.addEventListener('submit', function (event) {
        if (event.submitter && event.submitter.hasAttribute('formnovalidate')) return;
        const password = form.querySelector('[name="password"]');
        const confirm = form.querySelector('[name="confirm"]');
        if (confirm && password) {
            confirm.setCustomValidity(confirm.value !== password.value ? 'Passwords do not match.' : '');
        }
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    });
    form.querySelectorAll('input').forEach(function (input) {
        input.addEventListener('input', function () { input.setCustomValidity(''); });
    });
});
