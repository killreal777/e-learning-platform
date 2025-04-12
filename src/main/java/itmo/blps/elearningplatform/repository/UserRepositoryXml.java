package itmo.blps.elearningplatform.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import itmo.blps.elearningplatform.ELearningPlatformConfig;
import itmo.blps.elearningplatform.model.User;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryXml implements UserRepository {

    private final String path;
    private final XmlMapper xmlMapper;

    private final Map<Integer, User> data;
    private final AtomicInteger idSequence;

    @Autowired
    public UserRepositoryXml(ELearningPlatformConfig config) {
        this.path = config.security().usersDataPath();
        this.xmlMapper = new XmlMapper();
        this.data = new ConcurrentHashMap<>();
        this.idSequence = new AtomicInteger(1);
    }

    @PostConstruct
    private void init() {
        loadFromXml();
    }

    private synchronized void loadFromXml() {
        try {
            File file = new File(path);
            if (!file.exists() || file.length() == 0) return;
            List<User> data = xmlMapper.readValue(file, new TypeReference<>() {});
            this.data.clear();
            this.data.putAll(data.stream().collect(Collectors.toMap(
                    User::getId, Function.identity()
            )));
            data.stream()
                    .map(User::getId)
                    .max(Integer::compareTo)
                    .ifPresent(maxId -> this.idSequence.set(maxId + 1));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read users from XML file", e);
        }
    }

    private synchronized void saveToXml() {
        try {
            xmlMapper.writeValue(new File(path), new ArrayList<>(data.values()));
        } catch (IOException e) {
            throw new RuntimeException("Cannot write users to XML file", e);
        }
    }

    @Override
    public synchronized User save(User user) {
        if (user.getId() == null) {
            user.setId(idSequence.getAndIncrement());
        } else {
            idSequence.compareAndSet(user.getId(), user.getId() + 1);
        }
        data.put(user.getId(), user);
        saveToXml();
        return user;
    }

    @Override
    public List<User> findAll() {
        return data.values().stream()
                .toList();
    }

    @Override
    public Optional<User> findById(Integer id) {
        return data.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(User user) {
        data.remove(user.getId());
        saveToXml();
    }

    @Override
    public boolean existsByUsername(String username) {
        return data.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }

    @Override
    public boolean existsByRole(User.Role role) {
        return data.values().stream()
                .anyMatch(user -> user.getRole().equals(role));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return data.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
    }

    @Override
    public List<User> findAllByEnabledFalse() {
        return data.values().stream()
                .filter(user -> !user.isEnabled())
                .toList();
    }

    @Override
    public List<User> findAllByRole(User.Role role) {
        return data.values().stream()
                .filter(user -> user.getRole().equals(role))
                .toList();
    }

    @Override
    public Optional<User> findByIdAndRole(Integer id, User.Role role) {
        return data.values().stream()
                .filter(user -> user.getId().equals(id) && user.getRole().equals(role))
                .findAny();
    }
}
