$(document).ready(function() {
    const host = "/exchange"

    // Fetch the list of currencies and populate the select element
    function requestCurrencies() {
        $.ajax({
            url: `${host}/currencies`,
            type: "GET",
            dataType: "json",
            success: function (data) {
                const tbody = $('.currencies-table tbody');
                tbody.empty();
                $.each(data, function(index, currency) {
                    const row = $('<tr>');
                    row.append($('<td>').text(currency.code));
                    row.append($('<td>').text(currency.fullName));
                    row.append($('<td>').text(currency.sign));
                    tbody.append(row);
                });

                const newRateBaseCurrency = $("#new-rate-base-currency");
                newRateBaseCurrency.empty();
                $.each(data, function (index, currency) {
                    newRateBaseCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                const newRateTargetCurrency = $("#new-rate-target-currency");
                newRateTargetCurrency.empty();
                $.each(data, function (index, currency) {
                    newRateTargetCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                const convertBaseCurrency = $("#convert-base-currency");
                convertBaseCurrency.empty();
                $.each(data, function (index, currency) {
                    convertBaseCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                const convertTargetCurrency = $("#convert-target-currency");
                convertTargetCurrency.empty();
                $.each(data, function (index, currency) {
                    convertTargetCurrency.append(`<option value="${currency.code}">${currency.code}</option>`);
                });

                window.currenciesList = data;
            },
            error: function (jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }

    requestCurrencies();

    $("#add-currency").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: `${host}/currencies`,
            type: "POST",
            data: $("#add-currency").serialize(),
            success: function(data) {
                requestCurrencies();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });

    function requestExchangeRates() {
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "GET",
            dataType: "json",
            success: function(response) {
                const tbody = $('.exchange-rates-table tbody');
                tbody.empty();
                $.each(response, function(index, rate) {
                    const row = $('<tr>');
                    const currency = rate.baseCurrency.code + rate.targetCurrency.code;
                    const exchangeRate = rate.rate;

                    row.attr('data-pair', currency);
                    row.attr('data-base-id', rate.baseCurrency.id);
                    row.attr('data-target-id', rate.targetCurrency.id);

                    row.append($('<tr><td>').text(currency));
                    row.append($('<td><td>').text(exchangeRate));
                    row.append($('<td></td>').html(
                        '<button class="btn btn-secondary btn-sm exchange-rate-edit" data-bs-toggle="modal" data-bs-target="#edit-exchange-rate-modal">Edit</button>'
                    ));
                    tbody.append(row);
                });
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
    }

    requestExchangeRates();

    $(document).delegate('.exchange-rate-edit', 'click', function() {
        const pair = $(this).closest('tr').attr('data-pair');
        const exchangeRate = $(this).closest('tr').find('td:eq(1)').text();

        $('#edit-exchange-rate-modal').attr('data-pair', pair);
        $('#edit-exchange-rate-modal .modal-title').html(`<span translate="no" class="notranslate">Edit ${pair} Exchange Rate</span>`);
        $('#edit-exchange-rate-modal #exchange-rate-input').val(exchangeRate);
    });

    // Исправленный обработчик для кнопки Save
    $('#edit-exchange-rate-modal .btn-primary').click(function() {
        const pair = $('#edit-exchange-rate-modal').attr('data-pair');
        let exchangeRate = $('#edit-exchange-rate-modal #exchange-rate-input').val();

        // Замена запятой на точку
        exchangeRate = exchangeRate.replace(',', '.');

        if (!pair) {
            const toast = $('#api-error-toast');
            $(toast).find('.toast-body').text('Ошибка: код валют не найден');
            toast.toast("show");
            return;
        }

        // Проверка валидности курса
        if (isNaN(exchangeRate) || exchangeRate <= 0) {
            const toast = $('#api-error-toast');
            $(toast).find('.toast-body').text('Ошибка: введите корректный курс (положительное число)');
            toast.toast("show");
            return;
        }

        $.ajax({
            url: `${host}/exchangeRate/${pair}?rate=${exchangeRate}`,
            type: "PATCH",
            success: function() {
                requestExchangeRates();
                $('#edit-exchange-rate-modal').modal('hide');
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text('Курс успешно обновлен');
                toast.toast("show");
                setTimeout(function() {
                    toast.toast("hide");
                }, 2000);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                let errorMessage = 'Ошибка обновления курса';
                try {
                    const error = JSON.parse(jqXHR.responseText);
                    errorMessage = error.message || errorMessage;
                } catch(e) {
                    errorMessage = jqXHR.responseText || errorMessage;
                }
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(errorMessage);
                toast.toast("show");
                console.error('Error:', jqXHR.status, errorMessage);
            }
        });
    });

    $("#add-exchange-rate").submit(function(e) {
        e.preventDefault();
        $.ajax({
            url: `${host}/exchangeRates`,
            type: "POST",
            data: $("#add-exchange-rate").serialize(),
            success: function(data) {
                requestExchangeRates();
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });

    $("#convert").submit(function(e) {
        e.preventDefault();
        const baseCurrency = $("#convert-base-currency").val();
        const targetCurrency = $("#convert-target-currency").val();
        const amount = $("#convert-amount").val();

        if (!baseCurrency || !targetCurrency || !amount) {
            const toast = $('#api-error-toast');
            $(toast).find('.toast-body').text('Ошибка: заполните все поля');
            toast.toast("show");
            return;
        }

        $.ajax({
            url: `${host}/exchange?from=${baseCurrency}&to=${targetCurrency}&amount=${amount}`,
            type: "GET",
            success: function(data) {
                $("#convert-converted-amount").val(data.convertedAmount);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                const error = JSON.parse(jqXHR.responseText);
                const toast = $('#api-error-toast');
                $(toast).find('.toast-body').text(error.message);
                toast.toast("show");
            }
        });
        return false;
    });
});