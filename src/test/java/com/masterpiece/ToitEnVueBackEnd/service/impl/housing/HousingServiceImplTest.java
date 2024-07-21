package com.masterpiece.ToitEnVueBackEnd.service.impl.housing;

import com.masterpiece.ToitEnVueBackEnd.dto.housing.CreateHousingDto;
import com.masterpiece.ToitEnVueBackEnd.dto.housing.HousingDto;
import com.masterpiece.ToitEnVueBackEnd.model.file.File;
import com.masterpiece.ToitEnVueBackEnd.model.housing.Housing;
import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import com.masterpiece.ToitEnVueBackEnd.repository.file.FileRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.repository.user.UserRepository;
import com.masterpiece.ToitEnVueBackEnd.service.file.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HousingServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private HousingRepository housingRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks // j'injecte tous les mocks créés
    private HousingServiceImpl housingService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // initialise les mocks et les injecte dans housingService
    }


    @Test
    public void should_save_housing_with_files() {
        // Given
        // Création d'une instance de CreateHousingDto simulée
        CreateHousingDto createHousingDto = new CreateHousingDto();
        createHousingDto.setUser_id(1L); // Initialisation de l'ID utilisateur dans CreateHousingDto

        // Création d'une liste simulée de fichiers MultiPart
        List<MultipartFile> files = new ArrayList<>();

        // Mocking d'un fichier MultiPart
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("file1.jpg"); // Définition du nom de fichier simulé
        when(file.getContentType()).thenReturn("image/jpeg"); // Définition du type de contenu simulé
        files.add(file); // Ajout du fichier mocké à la liste de fichiers

        // Création d'un utilisateur simulé
        User user = new User();
        user.setId(1L); // Initialisation de l'ID utilisateur dans User

        // Création d'une instance de Housing simulée
        Housing housing = new Housing();
        housing.setUser(user); // Association de l'utilisateur à l'instance de Housing

        // Création d'une instance de Housing simulée pour la sauvegarde
        Housing createdHousing = new Housing();
        createdHousing.setUser(housing.getUser()); // Définition de l'utilisateur qui crée l'annonce
        createdHousing.setHousing_id(1); // Définition de l'ID du logement créé
        createdHousing.setFiles(new ArrayList<>()); // Initialisation de la liste de fichiers du logement créé

        // Création d'une instance de File simulée pour simuler les fichiers sauvegardés
        File savedFile = new File();
        savedFile.setFile_name("file1.jpg");
        savedFile.setContent_type("image/jpeg");
        savedFile.setHousing(createdHousing);

        // Création d'une instance de HousingDto simulée pour simuler le retour
        HousingDto housingDto = new HousingDto();

        // When
        // Mocking du repository pour retourner l'utilisateur simulé lorsque findUserById est appelé
        when(userRepository.findUserById(createHousingDto.getUser_id())).thenReturn(user);
        // Mocking du modelMapper pour mapper CreateHousingDto vers Housing et retourner l'instance de Housing simulée
        when(modelMapper.map(createHousingDto, Housing.class)).thenReturn(housing);
        // Mocking du housingRepository pour retourner l'instance de Housing simulée lors de la sauvegarde
        when(housingRepository.save(housing)).thenReturn(createdHousing);
        // Mocking du fileRepository pour retourner l'instance de File simulée lors de la sauvegarde
        when(fileRepository.save(any(File.class))).thenReturn(savedFile);
        // Mocking du modelMapper pour mapper l'instance de Housing simulée vers HousingDto et retourner l'instance de HousingDto simulée
        when(modelMapper.map(createdHousing, HousingDto.class)).thenReturn(housingDto);

        // Appel de la méthode à tester
        HousingDto result = housingService.saveHousingWithFiles(createHousingDto, files);

        // Then
        // Vérification des assertions
        assertNotNull(result); // Vérifie que le résultat n'est pas nul
        // Vérification que la méthode findUserById du userRepository a été appelée une fois avec l'ID utilisateur spécifié
        verify(userRepository, times(1)).findUserById(createHousingDto.getUser_id());
        // Vérification que la méthode save du housingRepository a été appelée une fois avec l'instance de Housing simulée
        verify(housingRepository, times(1)).save(housing);
        // Vérification que la méthode save du fileRepository a été appelée une fois avec n'importe quelle instance de File
        verify(fileRepository, times(1)).save(any(File.class));
        // Vérification que la méthode save du fileStorageService a été appelée une fois avec le fichier simulé
        verify(fileStorageService, times(1)).save(file);
        // Vérification que la méthode map du modelMapper a été appelée une fois pour mapper CreateHousingDto vers Housing
        verify(modelMapper, times(1)).map(createHousingDto, Housing.class);
    }


}