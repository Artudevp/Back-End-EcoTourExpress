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

    // Métodos de conversión entre Cliente y ClienteDTO
    public ClienteDTO convertToDTO(Cliente cliente) {
        return new ClienteDTO(
            cliente.getID_cliente(),
            cliente.getCedula(),
            cliente.getNombre_cli(),
            cliente.getEdad(),
            cliente.getGenero()
        );
    }

    public Cliente convertToEntity(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        cliente.setID_cliente(clienteDTO.getID_cliente());
        cliente.setCedula(clienteDTO.getCedula());
        cliente.setNombre_cli(clienteDTO.getNombre_cli());
        cliente.setEdad(clienteDTO.getEdad());
        cliente.setGenero(clienteDTO.getGenero());
        return cliente;
    }

    // Guardar un nuevo Cliente
    public ClienteDTO saveCliente(ClienteDTO clienteDTO) {
        Cliente cliente = convertToEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Obtener todos los Clientes
    public List<ClienteDTO> getAllClientes() {
        List<ClienteDTO> clientes = clienteRepository.findAllClientesDTO();
        return clientes.stream()
                       .collect(Collectors.toList());
    }

    // Obtener Cliente por ID
    public ClienteDTO getClienteById(int id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id));
        return convertToDTO(cliente);
    }

    // Actualizar un Cliente
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

    // Eliminar un Cliente
    public void deleteCliente(int id) {
        clienteRepository.deleteById(id);
    }

    // Añadir actividades al cliente con verificación de disponibilidad
    public ClienteDTO addActividadesToCliente(int id_cliente, List<Integer> actividadIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        for (Integer actividadId : actividadIds) {
            Actividad existingActividad = actividadRepository.findById(actividadId)
                    .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + actividadId));
    
            if (existingActividad.isDisponible()) {
                if (!cliente.getActividades().contains(existingActividad)) {
                    cliente.getActividades().add(existingActividad);
                    existingActividad.setCapacidad(existingActividad.getCapacidad() - 1);
                    if (existingActividad.getCapacidad() <= 0) {
                        existingActividad.setDisponible(false);
                    }
                    actividadRepository.save(existingActividad);
                }
            } else {
                throw new RuntimeException("La actividad no está disponible.");
            }
        }
    
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Obtener todas las actividades de un Cliente
    public List<Actividad> getActividadesOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getActividades();
    }

    // Eliminar una actividad del Cliente
    public ClienteDTO removeActividadFromCliente(int id_cliente, int id_actividad) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        Actividad actividad = actividadRepository.findById(id_actividad)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id_actividad));
        
        if (cliente.getActividades().contains(actividad)) {
            actividad.setCapacidad(actividad.getCapacidad() + 1);
            if (actividad.getCapacidad() > 0) {
                actividad.setDisponible(true);
            }
            actividadRepository.save(actividad);
            cliente.getActividades().remove(actividad);
        } else {
            throw new RuntimeException("La actividad no está asignada a este cliente.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Añadir rutas al cliente con verificación de disponibilidad
    public ClienteDTO addRutasToCliente(int id_cliente, List<Integer> rutaIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        for (Integer id_ruta : rutaIds) {
            Ruta rut = rutaRepository.findById(id_ruta)
                    .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
            
            if (rut.isDisponible()) {
                boolean capacidadSuficiente = true;
                for (Actividad actividad : rut.getActividades()) {
                    if (actividad.getCapacidad() <= 0) {
                        capacidadSuficiente = false;
                        break;
                    }
                }
                
                if (capacidadSuficiente) {
                    rut.setCapacidad(rut.getCapacidad() - 1);
                    if (rut.getCapacidad() <= 0) {
                        rut.setDisponible(false);
                    }
                    rutaRepository.save(rut);
                    
                    for (Actividad actividad : rut.getActividades()) {
                        actividad.setCapacidad(actividad.getCapacidad() - 1);
                        if (actividad.getCapacidad() <= 0) {
                            actividad.setDisponible(false);
                        }
                        actividadRepository.save(actividad);
                    }

                    cliente.getRutas().add(rut);
                } else {
                    throw new RuntimeException("No hay suficiente capacidad en las actividades de la ruta.");
                }
            } else {
                throw new RuntimeException("La ruta no está disponible.");
            }
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Obtener todas las rutas de un Cliente
    public List<Ruta> getRutasOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getRutas();
    }

    // Eliminar una ruta del Cliente
    public ClienteDTO removeRutaFromCliente(int id_cliente, int id_ruta) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        
        Ruta ruta = rutaRepository.findById(id_ruta)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta no encontrada con id: " + id_ruta));
        
        if (cliente.getRutas().contains(ruta)) {
            ruta.setCapacidad(ruta.getCapacidad() + 1);
            if (ruta.getCapacidad() > 0) {
                ruta.setDisponible(true);
            }
            rutaRepository.save(ruta);

            cliente.getRutas().remove(ruta);
        } else {
            throw new RuntimeException("La ruta no está asignada a este cliente.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Añadir productos al cliente con verificación de disponibilidad
    public ClienteDTO addProductosToCliente(int id_cliente, List<Integer> productoIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

        for (Integer productoId : productoIds) {
            Producto existingProducto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));

            if (existingProducto.getCantidad_disponible() > 0) {
                existingProducto.setCantidad_disponible(existingProducto.getCantidad_disponible() - 1);
                if (existingProducto.getCantidad_disponible() == 0) {
                    existingProducto.setDisponible(false);
                }
                productoRepository.save(existingProducto);

                cliente.getProductos().add(existingProducto);
            } else {
                throw new RuntimeException("El producto no está disponible o no tiene suficiente cantidad.");
            }
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Obtener todos los productos de un Cliente
    public List<Producto> getProductosOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getProductos();
    }

    // Eliminar un producto del Cliente
    public ClienteDTO removeProductosFromCliente(int id_cliente, List<Integer> productoIds) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
    
        for (Integer productoId : productoIds) {
            Producto producto = productoRepository.findById(productoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productoId));
    
            if (cliente.getProductos().contains(producto)) {
                producto.setCantidad_disponible(producto.getCantidad_disponible() + 1);
                if (producto.getCantidad_disponible() > 0) {
                    producto.setDisponible(true);
                }
                productoRepository.save(producto);
                cliente.getProductos().remove(producto);
            } else {
                throw new RuntimeException("El producto no está asignado a este cliente.");
            }
        }
    
        // Guardar el cliente actualizado y retornar el DTO correspondiente
        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente); // Asegúrate de que este método existe y convierte Cliente a ClienteDTO
    }
    
        

    // Añadir hospedaje al cliente con verificación de disponibilidad
    public ClienteDTO addHospedajeToCliente(int id_cliente, int hospedajeId) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

        Hospedaje existingHospedaje = hospedajeRepository.findById(hospedajeId)
                .orElseThrow(() -> new ResourceNotFoundException("Hospedaje no encontrado con id: " + hospedajeId));

        if (existingHospedaje.getCapacidad() > 0) {
            existingHospedaje.setCapacidad(existingHospedaje.getCapacidad() - 1);
            if (existingHospedaje.getCapacidad() == 0) {
                existingHospedaje.setDisponible(false);
            }
            hospedajeRepository.save(existingHospedaje);

            cliente.setHabitacion(existingHospedaje);
        } else {
            throw new RuntimeException("El hospedaje no está disponible o no tiene suficiente capacidad.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    // Obtener el hospedaje de un Cliente
    public Hospedaje getHospedajeOfCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));
        return cliente.getHabitacion();
    }

    // Eliminar el hospedaje del Cliente
    public ClienteDTO removeHospedajeFromCliente(int id_cliente) {
        Cliente cliente = clienteRepository.findById(id_cliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id: " + id_cliente));

        Hospedaje hospedaje = cliente.getHabitacion();
        if (hospedaje != null) {
            hospedaje.setCapacidad(hospedaje.getCapacidad() + 1);
            if (hospedaje.getCapacidad() > 0) {
                hospedaje.setDisponible(true);
            }
            hospedajeRepository.save(hospedaje);

            cliente.setHabitacion(hospedaje);
        } else {
            throw new RuntimeException("El cliente no tiene un hospedaje asignado.");
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }


}
