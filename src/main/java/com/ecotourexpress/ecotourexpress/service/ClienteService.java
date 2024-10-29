package com.ecotourexpress.ecotourexpress.service;

import com.ecotourexpress.ecotourexpress.model.Cliente;
import com.ecotourexpress.ecotourexpress.model.Hospedaje;
import com.ecotourexpress.ecotourexpress.model.Producto;
import com.ecotourexpress.ecotourexpress.config.exception.ResourceNotFoundException;
import com.ecotourexpress.ecotourexpress.model.Actividad;
import com.ecotourexpress.ecotourexpress.model.Ruta;
import com.ecotourexpress.ecotourexpress.model.DTO.ClienteDTO;
import com.ecotourexpress.ecotourexpress.repository.ClienteRepository;
import com.ecotourexpress.ecotourexpress.repository.HospedajeRepository;
import com.ecotourexpress.ecotourexpress.repository.ProductoRepository;
import com.ecotourexpress.ecotourexpress.repository.ActividadRepository;
import com.ecotourexpress.ecotourexpress.repository.RutaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    // Conexión a repositorios
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ActividadRepository actividadRepository;

    @Autowired
    private RutaRepository rutaRepository;

    @Autowired
    private HospedajeRepository hospedajeRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Método para convertir Cliente a ClienteDTO
    public ClienteDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getID_cliente(),
            cliente.getCedula(),
            cliente.getNombre_cli(),
            cliente.getEdad(),
            cliente.getGenero()
        );
    }
    

    // Método para convertir ClienteDTO a Cliente
    public Cliente convertToEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setID_cliente(clienteDTO.getID_cliente());
        cliente.setCedula(clienteDTO.getCedula());
        cliente.setNombre_cli(clienteDTO.getNombre_cli());
        cliente.setEdad(clienteDTO.getEdad());
        cliente.setGenero(clienteDTO.getGenero());
        return cliente;
    }

    // Guardar o actualizar cliente
    public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Obtener todos los clientes
    public List<ClienteDTO> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                       .map(this::convertToDTO)
                       .collect(Collectors.toList());
    }

    // Obtener cliente por ID
    public ClienteDTO getClienteById(int id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDTO(cliente);
    }

    // Editar Cliente
    public ClienteDTO updateCliente(int id, ClienteDTO clienteDTO) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));

        clienteExistente.setNombre_cli(clienteDTO.getNombre_cli());
        clienteExistente.setEdad(clienteDTO.getEdad());
        clienteExistente.setGenero(clienteDTO.getGenero());
        clienteExistente.setCedula(clienteDTO.getCedula());
    
        Cliente clienteActualizado = clienteRepository.save(clienteExistente);
        return convertToDTO(clienteActualizado);
    }
    

    // Eliminar cliente
    public void deleteCliente(int id) {
        clienteRepository.deleteById(id);
    }

    // Añadir actividades al cliente con verificación de capacidad
    public ClienteDTO addActividadesToCliente(int id_cliente, List<Integer> actividadIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        for (Integer actividadId : actividadIds) {
            Actividad existingActividad = actividadRepository.findById(actividadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + actividadId));
    
            // Verificar que la actividad no esté ya asociada al cliente
            if (!cliente.getActividades().contains(existingActividad)) {
                cliente.getActividades().add(existingActividad);
                // Ajusta la capacidad si es necesario
                existingActividad.setCapacidad(existingActividad.getCapacidad() - 1);
                actividadRepository.save(existingActividad); // Guardar cambios en la actividad
            }
        }
    
        // Guardar cambios en el cliente
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }
    
    // Obtener actividades del cliente
    public List<Actividad> getActividadesOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getActividades();
    }

    // Eliminar actividad del cliente y ajustar la capacidad
    public ClienteDTO removeActividadFromCliente(int id_cliente, int id_actividad) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        Actividad actividad = actividadRepository.findById(id_actividad)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id_actividad));
        
        // Verificar si la actividad pertenece al cliente antes de eliminarla
        if (cliente.getActividades().contains(actividad)) {
            // Incrementar la capacidad de la actividad
            actividad.setCapacidad(actividad.getCapacidad() + 1);
            actividadRepository.save(actividad); // Guardar cambios en la actividad

            // Eliminar la actividad del cliente
            cliente.getActividades().remove(actividad);
        } else {
            throw new RuntimeException("La actividad no está asignada a este cliente.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Añadir rutas al cliente
    public ClienteDTO addRutasToCliente(int id_cliente, List<Integer> rutaIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        for (Integer id_ruta : rutaIds) {
            Ruta rut = rutaRepository.findById(id_ruta)
                    .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
            
            // Verificar la capacidad de las actividades
            boolean capacidadSuficiente = true;
            for (Actividad actividad : rut.getActividades()) {
                if (actividad.getCapacidad() <= 0) {
                    capacidadSuficiente = false;
                    break;
                }
            }
            
            if (capacidadSuficiente) {
                // Decrementar la capacidad de la ruta
                rut.setCapacidad(rut.getCapacidad() - 1);
                rutaRepository.save(rut); // Guardar cambios en la ruta
                
                // Decrementar la capacidad de cada actividad
                for (Actividad actividad : rut.getActividades()) {
                    actividad.setCapacidad(actividad.getCapacidad() - 1);
                    actividadRepository.save(actividad); // Guardar cambios en la actividad
                }

                cliente.getRutas().add(rut); // Agregar la ruta al cliente
            } else {
                throw new RuntimeException("No hay suficiente capacidad en las actividades de la ruta.");
            }
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }
    
    // Obtener rutas del cliente
    public List<Ruta> getRutasOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getRutas();
    }

    // Eliminar ruta del cliente y ajustar la capacidad
    public ClienteDTO removeRutaFromCliente(int id_cliente, int id_ruta) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        Ruta ruta = rutaRepository.findById(id_ruta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
        
        // Verificar si la ruta pertenece al cliente antes de eliminarla
        if (cliente.getRutas().contains(ruta)) {
            // Incrementar la capacidad de la ruta
            ruta.setCapacidad(ruta.getCapacidad() + 1);
            rutaRepository.save(ruta); // Guardar cambios en la ruta

            // Incrementar la capacidad de cada actividad
            for (Actividad actividad : ruta.getActividades()) {
                actividad.setCapacidad(actividad.getCapacidad() + 1);
                actividadRepository.save(actividad); // Guardar cambios en la actividad
            }

            // Eliminar la ruta del cliente
            cliente.getRutas().remove(ruta);
        } else {
            throw new RuntimeException("La ruta no está asignada a este cliente.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Consultar hospedaje de cliente
    public Hospedaje getHospedajeOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getHabitacion();
    }

    // Asignar hospedaje a Cliente
    public ClienteDTO addHospedajeToCliente(int id_cliente, int id_hospedaje) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        // Buscar el hospedaje usando el ID
        Hospedaje hos = hospedajeRepository.findById(id_hospedaje)
                .orElseThrow(() -> new ResourceNotFoundException("Hospedaje no encontrado con id: " + id_hospedaje));
    
        // Verificar disponibilidad
        if (hos.getDisponibilidad() > 0) {
            cliente.setHabitacion(hos);
            hos.setDisponibilidad(hos.getDisponibilidad() - 1);
            hospedajeRepository.save(hos);
        } else {
            throw new IllegalStateException("No hay habitaciones disponibles para el hospedaje con id: " + id_hospedaje);
        }
    
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }
    
    // Desasignar hospedaje de cliente
    public ClienteDTO removeHospedajeFromCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        Hospedaje hospedaje = cliente.getHabitacion();
        if (hospedaje != null) {
            hospedaje.setDisponibilidad(hospedaje.getDisponibilidad() + 1); // Incrementar la disponibilidad
            hospedajeRepository.save(hospedaje); // Guardar cambios en el hospedaje
            cliente.setHabitacion(null); // Desasignar el hospedaje del cliente
        } else {
            throw new RuntimeException("Este cliente no tiene hospedaje asignado.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Consultar productos de cliente
    public List<Producto> getProductosOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getProductos();
    }

    // Añadir productos al cliente
    public ClienteDTO addProductosToCliente(int id_cliente, List<Integer> productoIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        for (Integer productoId : productoIds) {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
    
            // Verificar que el producto esté disponible
            if (producto.getCantidad_disponible() > 0) {
                cliente.getProductos().add(producto);
                producto.setCantidad_disponible(producto.getCantidad_disponible() - 1); // Decrementar la cantidad disponible
                productoRepository.save(producto); // Guardar cambios en el producto
            } else {
                throw new RuntimeException("Producto no disponible: " + producto.getNombre_p());
            }
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    public ClienteDTO removeProductoFromCliente(int id_cliente, List<Integer> id_productos) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        // Iterar sobre la lista de IDs de productos
        for (Integer id_producto : id_productos) {
            Producto producto = productoRepository.findById(id_producto)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id_producto));
    
            // Verificar que el producto esté asignado al cliente antes de eliminarlo
            if (cliente.getProductos().contains(producto)) {
                cliente.getProductos().remove(producto);
                producto.setCantidad_disponible(producto.getCantidad_disponible() + 1); // Incrementar la cantidad disponible
                productoRepository.save(producto); // Guardar cambios en el producto
            } else {
                throw new RuntimeException("El producto con ID " + id_producto + " no está asignado a este cliente.");
            }
        }
    
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }
    
}
