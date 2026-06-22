import { useEffect, useMemo, useState } from 'react';
import {
  createDdaa,
  deleteDdaa,
  fetchDdaaDetail,
  fetchDdaaFormOptions,
  fetchDdaaList,
  updateDdaa
} from '../services/ddaaService';

const emptyForm = {
  comunaId: '',
  rutTitular: '',
  instalacionId: '',
  fuenteId: '',
  nombreFuenteDerecho: '',
  naturalezaDerecho: 'Consuntivo',
  tipoDerecho: 'Aprovechamiento',
  estadoDerecho: 'Vigente'
};

function HomePage({ user }) {
  const [ddaa, setDdaa] = useState([]);
  const [selectedId, setSelectedId] = useState(null);
  const [detail, setDetail] = useState(null);
  const [options, setOptions] = useState({
    comunas: [],
    ruts: [],
    instalaciones: [],
    cuencas: [],
    subcuencas: [],
    fuentes: []
  });
  const [form, setForm] = useState(emptyForm);
  const [mode, setMode] = useState('create');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    loadInitialData();
  }, []);

  useEffect(() => {
    if (!selectedId) {
      setDetail(null);
      return;
    }

    fetchDdaaDetail(selectedId)
      .then(setDetail)
      .catch((loadError) => setError(loadError.message));
  }, [selectedId]);

  const selectedDdaa = useMemo(
    () => ddaa.find((item) => item.id === selectedId),
    [ddaa, selectedId]
  );

  async function loadInitialData() {
    setLoading(true);
    setError('');

    try {
      const [list, formOptions] = await Promise.all([
        fetchDdaaList(),
        fetchDdaaFormOptions()
      ]);
      setDdaa(list);
      setOptions({
        comunas: formOptions.comunas || [],
        ruts: formOptions.ruts || [],
        instalaciones: formOptions.instalaciones || [],
        cuencas: formOptions.cuencas || [],
        subcuencas: formOptions.subcuencas || [],
        fuentes: formOptions.fuentes || []
      });
      setSelectedId(list[0]?.id ?? null);
      setForm(defaultForm(formOptions));
    } catch (loadError) {
      setError(loadError.message);
    } finally {
      setLoading(false);
    }
  }

  function handleChange(event) {
    const { name, value } = event.target;
    setForm((current) => ({ ...current, [name]: value }));
  }

  function startCreate() {
    setMode('create');
    setForm(defaultForm(options));
    setMessage('');
    setError('');
  }

  function startEdit(item) {
    setMode('edit');
    setSelectedId(item.id);
    setMessage('');
    setError('');
    setForm({
      comunaId: item.comunaId || '',
      rutTitular: String(item.rutTitular || ''),
      instalacionId: item.instalacionId ? String(item.instalacionId) : '',
      fuenteId: String(item.fuenteId || ''),
      nombreFuenteDerecho: item.nombreFuenteDerecho || '',
      naturalezaDerecho: item.naturalezaDerecho || '',
      tipoDerecho: item.tipoDerecho || '',
      estadoDerecho: item.estadoDerecho || ''
    });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    setSaving(true);
    setMessage('');
    setError('');

    try {
      const payload = toPayload(form);
      if (mode === 'edit' && selectedId) {
        await updateDdaa(selectedId, payload);
        setMessage('Derecho actualizado.');
      } else {
        const created = await createDdaa(payload);
        setSelectedId(created.id);
        setMessage('Derecho creado.');
      }

      const list = await fetchDdaaList();
      setDdaa(list);
      if (mode !== 'edit') {
        setMode('create');
        setForm(defaultForm(options));
      }
    } catch (saveError) {
      setError(saveError.message);
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete(item) {
    const confirmed = window.confirm(`Eliminar DDAA #${item.id}?`);
    if (!confirmed) return;

    setSaving(true);
    setMessage('');
    setError('');

    try {
      await deleteDdaa(item.id);
      const list = await fetchDdaaList();
      setDdaa(list);
      setSelectedId(list[0]?.id ?? null);
      setMode('create');
      setForm(defaultForm(options));
      setMessage('Derecho eliminado.');
    } catch (deleteError) {
      setError(deleteError.message);
    } finally {
      setSaving(false);
    }
  }

  const hasRequiredCatalogs = options.comunas.length > 0 && options.ruts.length > 0 && options.fuentes.length > 0;

  return (
    <main className="app-shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">DDAA Platform</p>
          <h1>Gestión de derechos de agua</h1>
        </div>
        <div className="session-box">
          <span>{user.name || user.email}</span>
          <span className="status-pill">Sin autenticación / demo</span>
        </div>
      </header>

      {message && <div className="notice success">{message}</div>}
      {error && <div className="notice error">{error}</div>}

      <section className="dashboard-grid">
        <div className="list-panel">
          <div className="panel-header">
            <div>
              <h2>Derechos registrados</h2>
              <p>{loading ? 'Cargando registros' : `${ddaa.length} registros`}</p>
            </div>
            <button onClick={startCreate}>Nuevo</button>
          </div>

          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Titular</th>
                  <th>Fuente</th>
                  <th>Comuna</th>
                  <th>Estado</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {ddaa.map((item) => (
                  <tr
                    key={item.id}
                    className={item.id === selectedId ? 'selected-row' : ''}
                    onClick={() => setSelectedId(item.id)}
                  >
                    <td>#{item.id}</td>
                    <td>{item.titularNombre || item.rutTitular}</td>
                    <td>{item.nombreFuenteDerecho || item.fuenteNombre}</td>
                    <td>{item.comunaNombre || item.comunaId}</td>
                    <td><span className="status-pill">{item.estadoDerecho || 'Sin estado'}</span></td>
                    <td className="row-actions">
                      <button type="button" className="small-button" onClick={(event) => {
                        event.stopPropagation();
                        startEdit(item);
                      }}>Editar</button>
                      <button type="button" className="small-button danger" onClick={(event) => {
                        event.stopPropagation();
                        handleDelete(item);
                      }}>Eliminar</button>
                    </td>
                  </tr>
                ))}
                {!loading && ddaa.length === 0 && (
                  <tr>
                    <td colSpan="6" className="empty-state">No hay derechos registrados todavía.</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>

        <aside className="side-panel">
          <h2>{mode === 'edit' ? `Editar DDAA #${selectedId}` : 'Nuevo DDAA'}</h2>
          {!hasRequiredCatalogs && (
            <p className="form-warning">Faltan datos de catálogo para crear registros. Carga al menos comuna, titular y fuente.</p>
          )}
          <form className="ddaa-form" onSubmit={handleSubmit}>
            <label>
              Comuna
              <select name="comunaId" value={form.comunaId} onChange={handleChange} required>
                <option value="">Seleccionar</option>
                {options.comunas.map((comuna) => (
                  <option key={comuna.id} value={comuna.id}>{comuna.nombre}</option>
                ))}
              </select>
            </label>

            <label>
              Titular
              <select name="rutTitular" value={form.rutTitular} onChange={handleChange} required>
                <option value="">Seleccionar</option>
                {options.ruts.map((rut) => (
                  <option key={rut.rut} value={rut.rut}>{rut.nombre} - {rut.rut}</option>
                ))}
              </select>
            </label>

            <label>
              Instalación
              <select name="instalacionId" value={form.instalacionId} onChange={handleChange}>
                <option value="">Sin instalación</option>
                {options.instalaciones.map((instalacion) => (
                  <option key={instalacion.id} value={instalacion.id}>{instalacion.nombre}</option>
                ))}
              </select>
            </label>

            <label>
              Fuente
              <select name="fuenteId" value={form.fuenteId} onChange={handleChange} required>
                <option value="">Seleccionar</option>
                {options.fuentes.map((fuente) => (
                  <option key={fuente.id} value={fuente.id}>{fuente.nombre} ({fuente.tipo})</option>
                ))}
              </select>
            </label>

            <label>
              Nombre fuente derecho
              <input name="nombreFuenteDerecho" value={form.nombreFuenteDerecho} onChange={handleChange} required />
            </label>

            <div className="form-row">
              <label>
                Naturaleza
                <input name="naturalezaDerecho" value={form.naturalezaDerecho} onChange={handleChange} required />
              </label>
              <label>
                Tipo
                <input name="tipoDerecho" value={form.tipoDerecho} onChange={handleChange} required />
              </label>
            </div>

            <label>
              Estado
              <input name="estadoDerecho" value={form.estadoDerecho} onChange={handleChange} required />
            </label>

            <button type="submit" disabled={saving || !hasRequiredCatalogs}>
              {saving ? 'Guardando' : mode === 'edit' ? 'Guardar cambios' : 'Crear DDAA'}
            </button>
          </form>
        </aside>
      </section>

      <section className="detail-panel">
        <div className="panel-header">
          <div>
            <h2>Detalle</h2>
            <p>{selectedDdaa ? `DDAA #${selectedDdaa.id}` : 'Selecciona un registro'}</p>
          </div>
        </div>
        {detail?.ddaa ? (
          <div className="detail-grid">
            <Info label="Cuenca" value={detail.ddaa.cuencaNombre} />
            <Info label="Subcuenca" value={detail.ddaa.subcuencaNombre} />
            <Info label="Fuente" value={detail.ddaa.fuenteNombre} />
            <Info label="Tipo fuente" value={detail.ddaa.fuenteTipo} />
            <Info label="Expedientes" value={detail.expedientes?.length ?? 0} />
            <Info label="Pagos no uso" value={detail.pagosNoUso?.length ?? 0} />
            <Info label="Ejercicios" value={detail.ejercicios?.length ?? 0} />
          </div>
        ) : (
          <p className="empty-state">Sin detalle cargado.</p>
        )}
      </section>
    </main>
  );
}

function Info({ label, value }) {
  return (
    <div className="info-item">
      <span>{label}</span>
      <strong>{value || 'Sin dato'}</strong>
    </div>
  );
}

function defaultForm(options) {
  return {
    ...emptyForm,
    comunaId: options.comunas?.[0]?.id || '',
    rutTitular: options.ruts?.[0]?.rut ? String(options.ruts[0].rut) : '',
    instalacionId: options.instalaciones?.[0]?.id ? String(options.instalaciones[0].id) : '',
    fuenteId: options.fuentes?.[0]?.id ? String(options.fuentes[0].id) : '',
    nombreFuenteDerecho: options.fuentes?.[0]?.nombre || ''
  };
}

function toPayload(form) {
  return {
    comunaId: form.comunaId,
    rutTitular: Number(form.rutTitular),
    instalacionId: form.instalacionId ? Number(form.instalacionId) : null,
    fuenteId: Number(form.fuenteId),
    nombreFuenteDerecho: form.nombreFuenteDerecho,
    naturalezaDerecho: form.naturalezaDerecho,
    tipoDerecho: form.tipoDerecho,
    estadoDerecho: form.estadoDerecho
  };
}

export default HomePage;
