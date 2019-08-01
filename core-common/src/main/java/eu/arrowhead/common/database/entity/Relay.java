package eu.arrowhead.common.database.entity;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import eu.arrowhead.common.Defaults;
import eu.arrowhead.common.dto.RelayType;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"address", "port"}))
public class Relay {
	
	//=================================================================================================
	// members
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false, length = Defaults.VARCHAR_BASIC)
	private String address;
	
	@Column(nullable = false)
	private int port;
	
	@Column(nullable = false)
	private boolean secure = false;
	
	@Column(nullable = false)
	private boolean exclusive = false;
	
	@Column(nullable = false, columnDefinition = "varchar(" + Defaults.VARCHAR_BASIC + ") DEFAULT 'GENERAL_RELAY'")
	@Enumerated(EnumType.STRING)
	private RelayType type = RelayType.GENERAL_RELAY;
	
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private ZonedDateTime createdAt;
	
	@Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	private ZonedDateTime updatedAt;
	
	@OneToMany(mappedBy = "relay", fetch = FetchType.LAZY, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<CloudGatekeeperRelay> cloudGatekeepers = new HashSet<>();
	
	@OneToMany(mappedBy = "relay", fetch = FetchType.LAZY, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<CloudGatekeeperRelay> cloudGateways = new HashSet<>();

	//=================================================================================================
	// methods

	//-------------------------------------------------------------------------------------------------
	public Relay() {}

	//-------------------------------------------------------------------------------------------------
	public Relay(final String address, final int port, final boolean secure, final boolean exclusive, final RelayType type) {
		this.address = address;
		this.port = port;
		this.secure = secure;
		this.exclusive = exclusive;
		this.type = type;
	}
	
	//-------------------------------------------------------------------------------------------------
	@PrePersist
	public void onCreate() {
		this.createdAt = ZonedDateTime.now();
		this.updatedAt = this.createdAt;
	}
	
	//-------------------------------------------------------------------------------------------------
	@PreUpdate
	public void onUpdate() {
		this.updatedAt = ZonedDateTime.now();
	}
	
	//-------------------------------------------------------------------------------------------------
	public long getId() { return id; }
	public String getAddress() { return address; }
	public int getPort() { return port; }
	public boolean getSecure() { return secure; }
	public boolean getExclusive() { return exclusive; }
	public RelayType getType() { return type; }
	public ZonedDateTime getCreatedAt() { return createdAt; }
	public ZonedDateTime getUpdatedAt() { return updatedAt; }
	public Set<CloudGatekeeperRelay> getCloudGatekeepers() { return cloudGatekeepers; }
	public Set<CloudGatekeeperRelay> getCloudGateways() { return cloudGateways; }

	//-------------------------------------------------------------------------------------------------
	public void setId(final long id) { this.id = id; }
	public void setAddress(final String address) { this.address = address; }
	public void setPort(final int port) { this.port = port; }
	public void setSecure(final boolean secure) { this.secure = secure; }
	public void setExclusive (final boolean exclusive) { this.exclusive = exclusive; }
	public void setType(final RelayType type) { this.type = type; }
	public void setCreatedAt(final ZonedDateTime createdAt) { this.createdAt = createdAt; }
	public void setUpdatedAt(final ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
	public void setCloudGatekeepers(final Set<CloudGatekeeperRelay> cloudGatekeepers) { this.cloudGatekeepers = cloudGatekeepers; }
	public void setCloudGateways(final Set<CloudGatekeeperRelay> cloudGateways) { this.cloudGateways = cloudGateways; }

	//-------------------------------------------------------------------------------------------------
	@Override
	public String toString() {
		return "Relay [id = " + id + ", address = " + address + ", port = " + port + ", type = " + type + "]";
	}
}