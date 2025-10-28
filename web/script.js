const API_URL = 'http://localhost:8080/tipos-quarto';

        const listaUl = document.getElementById('lista-quartos');
        const form = document.getElementById('form-quarto');
        const formTitulo = document.getElementById('form-titulo');
        const inputId = document.getElementById('edit-id');
        const inputNome = document.getElementById('nome');
        const inputDescricao = document.getElementById('descricao');
        const inputCapacidade = document.getElementById('capacidade');
        const inputTarifa = document.getElementById('tarifa');
        const btnSalvar = document.getElementById('btn-salvar');
        const btnCancelar = document.getElementById('btn-cancelar');

        // --- 1. LISTAR (READ) ---
        async function listarQuartos() {
            try {
                const response = await fetch(API_URL);
                if (!response.ok) throw new Error('Falha na rede');
                const quartos = await response.json();

                listaUl.innerHTML = '';

                if (quartos.length === 0) {
                    listaUl.innerHTML = '<li>Nenhum quarto cadastrado.</li>';
                } else {
                    quartos.forEach(quarto => {
                        const li = document.createElement('li');
                        li.innerHTML = `
                            <strong>${quarto.nome} (ID: ${quarto.id})</strong>
                            <span>${quarto.descricao}</span><br>
                            <span>Capacidade: ${quarto.capacidadeMaxima} pessoas</span><br>
                            <span>Tarifa: R$ ${quarto.tarifaPadrao.toFixed(2)}</span>
                            <div class="botoes-acao">
                                <button class="btn-edit" onclick='prepararEdicao(${JSON.stringify(quarto)})'>Editar</button>
                                <button class="btn-delete" onclick="deletarQuarto(${quarto.id})">Deletar (X)</button>
                            </div>
                        `;
                        listaUl.appendChild(li);
                    });
                }
            } catch (error) {
                console.error('Erro ao buscar quartos:', error);
                listaUl.innerHTML = '<li>Erro ao carregar quartos. A API está rodando?</li>';
            }
        }

        // --- 2. SALVAR (CREATE) --
        async function salvarQuarto(event) {
            event.preventDefault();

            const id = inputId.value; 
            const nome = inputNome.value;
            const descricao = inputDescricao.value;
            const capacidade = inputCapacidade.value;
            const tarifa = inputTarifa.value;

            const quartoDto = {
                nome: nome,
                descricao: descricao,
                capacidadeMaxima: parseInt(capacidade),
                tarifaPadrao: parseFloat(tarifa)
            };

            const isEdicao = id !== '';
            const url = isEdicao ? `${API_URL}/${id}` : API_URL;
            const method = isEdicao ? 'PUT' : 'POST';

            try {
                const response = await fetch(url, {
                    method: method,
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(quartoDto),
                });

                if (!response.ok) {
                    const erro = await response.json(); 
                    throw new Error(erro.message || 'Erro ao salvar');
                }

                resetarFormulario();
                listarQuartos(); 

            } catch (error) {
                console.error('Erro ao salvar quarto:', error);
                alert(`Erro ao salvar: ${error.message}`);
            }
        }

        // --- 3. DELETAR (DELETE) ---
        async function deletarQuarto(id) {
            if (!confirm(`Tem certeza que deseja inativar (deletar) o quarto ID: ${id}?`)) {
                return; 
            }

            try {
                const response = await fetch(`${API_URL}/${id}`, {
                    method: 'DELETE'
                });

                if (!response.ok) { 
                    const erro = await response.json();
                    throw new Error(erro.message || 'Erro ao deletar');
                }

                listarQuartos();

            } catch (error) {
                console.error('Erro ao deletar quarto:', error);
                alert(`Erro ao deletar: ${error.message}`);
            }
        }

        // --- 4. EDIÇÃO DE FORMULÁRIO (UPDATE) ---

        function prepararEdicao(quarto) {
            window.scrollTo(0, 0); 
            
        
            formTitulo.innerText = `Editando Quarto (ID: ${quarto.id})`;
            btnSalvar.innerText = 'Atualizar Quarto';
            btnCancelar.style.display = 'block'; 

            inputId.value = quarto.id;
            inputNome.value = quarto.nome;
            inputDescricao.value = quarto.descricao;
            inputCapacidade.value = quarto.capacidadeMaxima;
            inputTarifa.value = quarto.tarifaPadrao;
        }

        function resetarFormulario() {
            formTitulo.innerText = 'Adicionar Novo Quarto';
            btnSalvar.innerText = 'Salvar Quarto';
            btnCancelar.style.display = 'none';
            
            form.reset(); 
            inputId.value = ''; 
        }

        

        document.addEventListener('DOMContentLoaded', listarQuartos);
        form.addEventListener('submit', salvarQuarto);
        btnCancelar.addEventListener('click', resetarFormulario);